package com.emanuel.fridolin.config;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigurationFile {

    private static final String SEPARATOR_CHAR = ":";

    private static final File ROOT_DIR = getRootDir();
    private final File configFile;

    public ConfigurationFile(String filename) {
        File configDir = new File(ROOT_DIR, "config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        this.configFile = new File(configDir, filename);
    }

    public static File getRootDir() {
        try {
            return new File(ConfigurationManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    public static void purgeOrphans(List<String> linkedNames) {
        File[] childArray = new File(ROOT_DIR, "config").listFiles();
        if (childArray != null) {
            List<File> childFiles = new ArrayList<>(Arrays.asList(childArray));
            childFiles.removeIf(file -> linkedNames.contains(file.getName()));
            childFiles.forEach(File::delete);
        }
    }

    public void writeFile(Map<String, String> propertyList) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));

        if (!configFile.exists()) {
            configFile.createNewFile();
        }

        for (Map.Entry<String, String> entry : propertyList.entrySet()) {
            String property = entry.getKey();
            String value = entry.getValue();

            String escapedProperty = property.replace(SEPARATOR_CHAR, "\\" + SEPARATOR_CHAR);
            String escapedValue = value.replace(SEPARATOR_CHAR, "\\" + SEPARATOR_CHAR);

            writer.write(escapedProperty + ":" + escapedValue + System.lineSeparator());
        }

        writer.close();
    }

    public Map<String, String> parseFile() throws IOException {
        Map<String, String> properties = new HashMap<>();

        if (configFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("(?<!\\\\):");

                String splitProperty = parts[0];
                String splitValue = parts[1];

                String newProperty = splitProperty.replace("\\:", ":");
                String newValue = splitValue.replace("\\:", ":");

                properties.put(newProperty, newValue);
            }
        }

        return properties;
    }

    public void deleteInvalidFile() {
        configFile.delete();
    }

}
