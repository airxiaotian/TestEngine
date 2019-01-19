package com.hitachi.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PropertiesUtils {
    public static List<ResourceBundle> getAllProperties() {
        File dir = new File("./target/classes");
        List<ResourceBundle> resources = new LinkedList<ResourceBundle>();
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".properties")) {
                resources.add(ResourceBundle.getBundle(file.getName().substring(0, file.getName().indexOf(".")),
                        Locale.JAPANESE));
            }
        }
        return resources;
    }

    public static ResourceBundle getProperties(String name) {
        return ResourceBundle.getBundle(name);
    }
}
