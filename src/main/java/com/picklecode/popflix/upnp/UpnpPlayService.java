/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.picklecode.popflix.upnp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bruno
 */
public class UpnpPlayService {

    private static final Logger LOG = LoggerFactory.getLogger(UpnpPlayService.class);

    private final UpnpDevice device;

    public UpnpPlayService(UpnpDevice device) {

        this.device = device;
    }

    public void play() {

        try {
            URL obj = new URL(device.getAVTransportUrl());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "TCAST");
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            con.setRequestProperty("SoapAction", "\"SOpnp-org:service:AVTransport:1#Play\"");

            String query = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"> <s:Body><u:Play xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><Speed>1</Speed></u:Play></s:Body></s:Envelope>";
            con.setRequestProperty("Content-Length", Integer.toString(query.length()));
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(query);
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            LOG.info("Sending 'POST' request to URL : " + device.getAVTransportUrl());
            LOG.info("Post parameters : " + query);
            LOG.info("Response Code : " + responseCode);
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                LOG.info(response.toString());
            }

        } catch (Exception ex) {
            LOG.error(ex.getMessage());

        }
    }

    public void stop() {

        try {

            URL obj = new URL(device.getAVTransportUrl());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "TCAST");
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            con.setRequestProperty("SoapAction", "\"SOpnp-org:service:AVTransport:1#Stop\"");

            String query = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"> <s:Body><u:Stop xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID></u:Stop></s:Body></s:Envelope>";

            con.setRequestProperty("Content-Length", Integer.toString(query.length()));
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(query);
                wr.flush();
            } catch (IOException ex) {
                LOG.error(ex.getMessage());
            }

            int responseCode = con.getResponseCode();
            LOG.info("Sending 'POST' request to URL : " + device.getAVTransportUrl());
            LOG.info("Post parameters : " + query);
            LOG.info("Response Code : " + responseCode);

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            LOG.info(response.toString());

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }

    }

    public void setAVTransportURI(String file) {

        try {

            URL obj = new URL(device.getAVTransportUrl());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "TCAST");
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            con.setRequestProperty("SoapAction", "\"SOpnp-org:service:AVTransport:1#SetAVTransportURI\"");

            String query = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>"
                    + "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
                    + "<s:Body><u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">"
                    + "<InstanceID>0</InstanceID>"
                    + "<CurrentURI>" + file + "</CurrentURI>"
                    + "<CurrentURIMetaData>&lt;DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sec=\"http://www.sec.co.kr/\"&gt;&lt;item id=\"f-0\" parentID=\"0\" restricted=\"0\"&gt;&lt;dc:title&gt;Video&lt;/dc:title&gt;&lt;dc:creator&gt;vGet&lt;/dc:creator&gt;&lt;upnp:class&gt;object.item.videoItem&lt;/upnp:class&gt;&lt;res protocolInfo=\"http-get:*:video/mp4:DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=01700000000000000000000000000000\""
                    + " &gt;" + file + "&lt;/res&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;</CurrentURIMetaData></u:SetAVTransportURI></s:Body></s:Envelope>";

            con.setRequestProperty("Content-Length", Integer.toString(query.length()));

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(query);
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            LOG.info("Sending 'POST' request to URL : " + device.getAVTransportUrl());
            LOG.info("Post parameters : " + query);
            LOG.info("Response Code : " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                LOG.info(response.toString());
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }

    }

}
