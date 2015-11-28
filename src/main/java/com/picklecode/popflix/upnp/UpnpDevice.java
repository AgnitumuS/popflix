/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.picklecode.popflix.upnp;

/**
 *
 * @author bruno
 */
public class UpnpDevice {
    
    final private String url;
    final private String name;
    final private String avTransport;
    private final String connectionManager;
    private final String renderingControl;

    public UpnpDevice(String url, String name, String avtransport, String connectionManager, String renderingControl) {
        this.url = url;
        this.name = name;
        this.avTransport = avtransport;
        this.connectionManager = connectionManager;
        this.renderingControl = renderingControl;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

 

    /**
     * @return the control
     */
    private String getAVTransport() {
        return avTransport;
    }

    
    public String getAVTransportUrl(){
        
        return getUrl()+getAVTransport().substring(1);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the connectionManager
     */
    private String getConnectionManager() {
        return connectionManager;
    }

    /**
     * @return the renderingControl
     */
    private String getRenderingControl() {
        return renderingControl;
    }
    
    public String getConnectionManagerUrl(){
        
        return getUrl()+getAVTransport().substring(1);
    }
    
    public String getRenderingControlUrl(){
        
        return getUrl()+getRenderingControl().substring(1);
    }
    
    
    
}
