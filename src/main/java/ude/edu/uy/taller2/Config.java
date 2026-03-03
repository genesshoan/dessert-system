package ude.edu.uy.taller2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase utilitaria para cargar configuración de la aplicación desde
 * el archivo `config/config.properties`. Provee valores por defecto cuando
 * el archivo no está disponible.
 */
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

    /**
     * Host del servidor configurado.
     *
     * @return host como cadena
     */
    public static String getHost() { return properties.getProperty("server.host"); }

    /**
     * Puerto configurado para el registro RMI.
     *
     * @return puerto como entero
     */
    public static int getPort() {
        try {
            return Integer.parseInt(properties.getProperty("server.port", "1099"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid port format, using default 1099");
            return 1099;
        }
    }

    /**
     * Nombre del servicio RMI.
     *
     * @return nombre del servicio
     */
    public static String getName() { return properties.getProperty("server.name"); }

    /**
     * Construye la URL de registro RMI a partir de la configuración.
     *
     * @return URL RMI en formato //host:port/name
     */
    public static String getURL() {
        return "//" + getHost() + ":" + getPort() + "/" + getName();
    }
}
