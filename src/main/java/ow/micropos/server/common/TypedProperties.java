package ow.micropos.server.common;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class TypedProperties {

    /*=======================================================================*
     =                                                                       =
     = Factory methods
     =                                                                       =
     *=======================================================================*/

    public static TypedProperties fromResource(String resource) {
        InputStream in = TypedProperties.class.getResourceAsStream("/" + resource);
        return new TypedProperties(in);
    }

    public static TypedProperties fromPath(String path) {
        try {
            InputStream in = new FileInputStream(path);
            return new TypedProperties(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static TypedProperties fromFile(File file) {
        try {
            InputStream in = new FileInputStream(file);
            return new TypedProperties(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /******************************************************************
     *                                                                *
     * Properties wrapper for type casting and store order
     *
     * The Properties class enumerates its keys (for storing) with
     * the keys() method. A TreeSet orders its items using either
     * their Comparable implementation or a Comparator. We overwrite
     * the keys() method to return a reordered enumeration.
     *                                                                *
     ******************************************************************/

    private final Properties properties = new Properties() {
        @Override
        public synchronized Enumeration<Object> keys() {
            return Collections.enumeration(new TreeSet<>(super.keySet()));
        }
    };

    public TypedProperties() {}

    public TypedProperties(InputStream in) {
        try {
            properties.load(in);

        } catch (IOException e) {
            System.out.println("Error loading properties.");
            throw new RuntimeException(e.getMessage());

        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public void save(File file) {

        try (FileOutputStream fos = new FileOutputStream(file)) {
            properties.store(fos, "AUTO GENERATED");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getProperties() {
        Enumeration<?> keyEnum = properties.propertyNames();
        List<String> keyList = new ArrayList<>();
        while (keyEnum.hasMoreElements())
            keyList.add(keyEnum.nextElement().toString());
        return keyList;
    }

    public List<String> getProperties(String prefix) {
        Enumeration<?> keyEnum = properties.propertyNames();
        List<String> keyList = new ArrayList<>();
        while (keyEnum.hasMoreElements()) {
            String key = keyEnum.nextElement().toString();
            if (key.startsWith(prefix))
                keyList.add(key);
        }
        return keyList;
    }

    public void add(String property, String value) {
        properties.put(property, value);
    }

    public boolean has(String property) {
        return properties.getProperty(property) != null;
    }

    public String getStr(String property) {
        return properties.getProperty(property);
    }

    public Integer getInt(String property) {
        return Integer.parseInt(properties.getProperty(property));
    }

    public Double getDbl(String property) {
        return Double.parseDouble(properties.getProperty(property));
    }

    public Long getLng(String property) {
        return Long.parseLong(properties.getProperty(property));
    }

    public BigDecimal getBd(String property) {
        return new BigDecimal(properties.getProperty(property));
    }

    public <E extends Enum<E>> E getEnum(Class<E> type, String property) {
        return Enum.valueOf(type, properties.getProperty(property));
    }

}
