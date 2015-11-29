/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.picklecode.popflix;

import com.picklecode.popflix.ui.Popflix;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.UIManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bruno
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static String OS = System.getProperty("os.name").toLowerCase();
    public static String ARCH = System.getProperty("os.arch").toLowerCase();

    static {
        try {
            loadLib("libjlibtorrent");
        } catch (UnsatisfiedLinkError e) {
            LOG.error(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {

            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Popflix.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Popflix().setVisible(true);
            }
        });
    }

    private static void loadLib(String name) {

        try {

            if (isWindows()) {
                name = name.substring("lib".length());
            }

            String ext = getExtension();
            name = name + ext;

            LOG.info(System.getProperty("os.arch"));
            LOG.info(System.getProperty("os.name"));
            Path tmp = Files.createTempDirectory("popflix");
            setLibraryPath(tmp.toString());
            LOG.info(tmp.toString() + "/" + name);
            File fileOut = new File(tmp.toString() + "/" + name);

            LOG.info(System.getProperty("java.library.path"));

            System.out.println("/lib/" + getFolder() + "/" + name);
            InputStream in = Popflix.class.getResourceAsStream("/lib/" + getFolder() + "/" + name);
            if (in != null) {

                OutputStream out = FileUtils.openOutputStream(fileOut);
                IOUtils.copy(in, out);
                in.close();
                out.close();

            }
            System.load(fileOut.getAbsolutePath());//loading goes here
        } catch (Exception e) {
            LOG.error(e.getMessage());
            System.exit(-1);
        }
    }

    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix64() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) && ARCH.indexOf("64") >= 0;

    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) && ARCH.indexOf("64") == -1;

    }

    private static String getExtension() {
        if (isWindows()) {
            return ".dll";
        }

        if (isUnix() || isUnix64()) {
            return ".so";
        }

        if (isMac()) {
            return ".dylib";
        }

        return "";
    }

    private static String getFolder() {
        if (isUnix64()) {
            return "x86_64";
        }

        if (isUnix() || isWindows()) {
            return "x86";
        }

        return "";
    }

    public static void setLibraryPath(String path) throws Exception {
        System.setProperty("java.library.path", path);

        //set sys_paths to null so that java.library.path will be reevalueted next time it is needed
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }
}
