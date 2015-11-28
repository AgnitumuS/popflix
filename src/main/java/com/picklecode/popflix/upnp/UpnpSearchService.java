/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.picklecode.popflix.upnp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author bruno
 */
public class UpnpSearchService extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(UpnpSearchService.class);

    private final Map<String, UpnpDevice> devices = new HashMap();
    private final List<UpnpSearchListener> listeners = new ArrayList<>();

    @Override
    public void run() {

        try {
            while (true) {
                discovery();
                Thread.sleep(5000);
            }

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
    }

    private void discovery() throws Exception {
        // SSDP port
        final int SSDP_PORT = 1900;
        final int SSDP_SEARCH_PORT = 1901;
        // Broadcast address for finding routers.
        final String SSDP_IP = "239.255.255.250";
        // Time out of the connection.
        int TIMEOUT = 5000;

        // Send from localhost:1901
        InetSocketAddress srcAddress = new InetSocketAddress(SSDP_SEARCH_PORT);
        // Send to 239.255.255.250:1900
        InetSocketAddress dstAddress = new InetSocketAddress(InetAddress.getByName(SSDP_IP), SSDP_PORT);

        // ----------------------------------------- //
        //       Construct the request packet.       //
        // ----------------------------------------- //
        StringBuilder discoveryMessage = new StringBuilder();
        discoveryMessage.append("M-SEARCH * HTTP/1.1\r\n");
        discoveryMessage.append("HOST: " + SSDP_IP + ":" + SSDP_PORT + "\r\n");
        discoveryMessage.append("ST: urn:schemas-upnp-org:device:MediaRenderer:1\r\n");
        // ST: urn:schemas-upnp-org:service:WANIPConnection:1\r\n
        discoveryMessage.append("MAN: \"ssdp:discover\"\r\n");
        discoveryMessage.append("MX: 5\r\n");
        discoveryMessage.append("\r\n");

        byte[] discoveryMessageBytes = discoveryMessage.toString().getBytes();
        DatagramPacket discoveryPacket = new DatagramPacket(discoveryMessageBytes, discoveryMessageBytes.length, dstAddress);

        // ----------------------------------- //
        //       Send multi-cast packet.       //
        // ----------------------------------- //
        MulticastSocket multicast = null;
        try {
            multicast = new MulticastSocket(null);
            multicast.bind(srcAddress);
            multicast.setTimeToLive(4);

            final MulticastSocket finalMulticast = multicast;

            finalMulticast.send(discoveryPacket);

        } finally {

            multicast.disconnect();
            multicast.close();
        }

        // -------------------------------------------------- //
        //       Listening to response from the router.       //
        // -------------------------------------------------- //
        DatagramSocket wildSocket = null;
        DatagramPacket receivePacket = null;
        try {
            wildSocket = new DatagramSocket(SSDP_SEARCH_PORT);
            wildSocket.setSoTimeout(TIMEOUT);

            try {

                receivePacket = new DatagramPacket(new byte[1536], 1536);
                wildSocket.receive(receivePacket);
                String message = new String(receivePacket.getData());
                LOG.info("Device Found: " + message);
                String[] data = message.split("\r\n");

                for (String line : data) {
                    if (line.startsWith("Location")) {

                        String url = line.substring(line.indexOf(":") + 1).trim();
                        sendGet(url);
                    }

                }
            } catch (SocketTimeoutException e) {
                LOG.error(e.getMessage());
            }

        } finally {
            if (wildSocket != null) {
                wildSocket.disconnect();
                wildSocket.close();
            }
        }

    }

    private void sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", "TCAST");

        Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

        NodeList controls = d.getElementsByTagName("controlURL");

        String avTranportUrl = null;
        String connectionManagerUrl = null;
        String renderingControlUrl = null;
        for (int i = 0; i < controls.getLength(); i++) {
            String s = controls.item(i).getFirstChild().getTextContent();
            if (s.contains("AVTransport")) {
                avTranportUrl = s;
            } else if (s.contains("ConnectionManager")) {
                connectionManagerUrl = s;
            } else if (s.contains("RenderingControl")) {
                renderingControlUrl = s;
            }
        }
        String name = d.getElementsByTagName("friendlyName").item(0).getFirstChild().getTextContent();
        con.getInputStream().close();

        if (!devices.containsKey(name)) {
            devices.put(name, new UpnpDevice(url, name, avTranportUrl, connectionManagerUrl, renderingControlUrl));

            for (UpnpSearchListener l : this.listeners) {
                l.onDeviceFound(devices.get(name));

            }
        }

    }

    public void addUpnpSearchListener(UpnpSearchListener l) {
        listeners.add(l);
    }

    public String[] getDevicesNames() {
        return devices.keySet().toArray(new String[devices.size()]);
    }

    public UpnpDevice getDevice(String name) {
        return devices.get(name);
    }

}
