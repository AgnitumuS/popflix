
package com.picklecode.popflix.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bruno
 */
public class PropertiesUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    private static PropertiesUtil _instance = null;
    Properties prop;

    private PropertiesUtil() {

        this.prop = new Properties();
        try {

            InputStream is = new FileInputStream(POPFLIX_PROPERTIES);
            prop.load(is);

        } catch (FileNotFoundException e) {
            LOG.warn(e.getMessage());
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
    private static final String POPFLIX_PROPERTIES = "popflix.properties";

    public static PropertiesUtil get() {
        if (_instance == null) {
            _instance = new PropertiesUtil();
        }

        return _instance;
    }

    public String getServerPort() {
        return this.prop.getProperty(SERVER_PORT, "0");
    }
    public static final String SERVER_PORT = "server.port";

    public String getSaveLocation() {
        return this.prop.getProperty(SAVE_LOCATION, System.getProperty("java.io.tmpdir"));
    }
    public static final String SAVE_LOCATION = "save.location";

    public void setServerPort(String port) {
        this.prop.setProperty(SERVER_PORT, port);
    }

    public void setSaveLocation(String file) {
        this.prop.setProperty(SAVE_LOCATION, file);
    }

    public void save() {
        try {
            this.prop.store(new FileOutputStream(POPFLIX_PROPERTIES), null);
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
    }

}
