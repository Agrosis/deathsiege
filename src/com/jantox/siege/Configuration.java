package com.jantox.siege;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public static Configuration config;

    private Properties props;

    public Configuration() {
        props = new Properties();

        if(!(new File("config").exists())) {
            this.createDefaultConfig();
        } else { // load existing configuration
            try {
                props.load(new FileInputStream("config"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createDefaultConfig() {
        props.setProperty("ip", "50.142.204.232");
        props.setProperty("port", "25565");

        try {
            props.store(new FileOutputStream("config"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public static void init() {
        config  = new Configuration();
    }

    public static String getProperty(String key) {
        return config.get(key);
    }

}

