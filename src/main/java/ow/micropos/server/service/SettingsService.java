package ow.micropos.server.service;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ow.micropos.server.exception.InvalidParameterException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// TODO: Redesign settings service

@Service
public class SettingsService {

    private FileSystemResource settingsResource;
    private Properties settingsProperties;

    @PostConstruct
    private void init() {
        //settingsResource = new FileSystemResource(file);
        //settingsProperties = new Properties();
        //loadSettings();
    }

    public void printSettings() {
        settingsProperties.list(System.out);
    }

    public void loadSettings() {
        InputStream is = null;

        try {
            is = settingsResource.getInputStream();
            settingsProperties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public void saveSettings() {
        OutputStream os = null;

        try {
            os = settingsResource.getOutputStream();
            settingsProperties.store(os, "MicroPOS Settings");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    public Map<String, String> getSettings(String... settingNames) {
        return null;
        /*
        if (settingNames == null)
            throw new InvalidParameterException("Must request at least one setting.");

        Map<String, String> settings = new HashMap<>(settingNames.length);

        for (String settingName : settingNames)
            settings.put(settingName, settingsProperties.getProperty(settingName));

        return settings;
        */
    }

    public void putSetting(String key, String value) {
        //settingsProperties.setProperty(key, value);
        //saveSettings();
    }

}
