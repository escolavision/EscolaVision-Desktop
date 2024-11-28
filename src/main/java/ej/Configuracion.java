package ej;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase que gestiona la configuración de la aplicación.
 * Carga y guarda propiedades desde un archivo de configuración.
 */
public class Configuracion {
    private Properties propiedades = new Properties();
    private String ip;
    private String port;
    private String bbdd;
    private String user;
    private String pwd;

    public Configuracion() throws IOException {
        cargarConfiguracion();
    }

    private void cargarConfiguracion() throws IOException {
        String rutaRelativa = "src/main/resources/config.properties";
        File archivoConfig = new File(rutaRelativa);
        if (archivoConfig.exists()) {
            try (InputStream input = new FileInputStream(archivoConfig)) {
                propiedades.load(input);
                ip = propiedades.getProperty("IP");
                port = propiedades.getProperty("Port");
                bbdd = propiedades.getProperty("BBDD");
                user = propiedades.getProperty("Username");
                pwd = propiedades.getProperty("Pwd");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("El archivo no existe en la ruta especificada.");
        }

    }

    public String getIP() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getBbdd() {
        return bbdd;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }
}
