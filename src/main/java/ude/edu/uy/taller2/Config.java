package ude.edu.uy.taller2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String FILE = "config/config.properties";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream(FILE)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("No config.properties file was found, using default values");
            properties.setProperty("server.host", "localhost");
            properties.setProperty("server.port", "1099");
            properties.setProperty("server.name", "DessertSystem");
        }
    }

    public static String getHost() { return properties.getProperty("server.host"); }
    public static String getPort() { return properties.getProperty("server.port"); }
    public static String getName() { return properties.getProperty("server.name"); }

    public static String getURL() {
        return "//" + getHost() + ":" + getPort() + "/" + getName();
    }
}
