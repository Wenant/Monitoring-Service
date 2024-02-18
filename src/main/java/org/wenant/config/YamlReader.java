package org.wenant.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlReader {
    private static Map<String, Object> yamlMap;

    private YamlReader() {
    }

    private static void initialize() {
        InputStream inputStream = YamlReader.class.getClassLoader().getResourceAsStream("application.yml");

        Yaml yaml = new Yaml();
        yamlMap = yaml.load(inputStream);
    }

    private static String getValue(String section, String key) {
        if (yamlMap == null) {
            initialize();
        }

        if (yamlMap != null && yamlMap.containsKey(section)) {
            Map<String, Object> sectionMap = (Map<String, Object>) yamlMap.get(section);
            if (sectionMap.containsKey(key)) {
                return (String) sectionMap.get(key);
            } else {
                throw new RuntimeException("Отсутствует ключ '" + key + "' в разделе '" + section + "'");
            }
        } else {
            throw new RuntimeException("Отсутствует раздел '" + section + "' в файле YAML");
        }
    }

    public static String getJwtSecret() {
        return getValue("jwt", "secret");
    }

    public static String getDbUrl() {
        return getValue("db", "url");
    }

    public static String getDbUsername() {
        return getValue("db", "username");
    }

    public static String getDbPassword() {
        return getValue("db", "password");
    }

    public static String getDbDriver() {
        return getValue("db", "driver");
    }
}