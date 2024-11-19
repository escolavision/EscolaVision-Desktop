package ej;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    public Configuracion() {
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        String rutaRelativa = System.getProperty("user.dir") + "/config.properties";
        try (InputStream input = new FileInputStream(rutaRelativa)) {
            propiedades.load(input);
            ip = propiedades.getProperty("IP");
            port = propiedades.getProperty("Port");
            bbdd = propiedades.getProperty("BBDD");
            user = propiedades.getProperty("Username");
            pwd = propiedades.getProperty("Pwd");
        } catch (IOException ex) {
            ex.printStackTrace();
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
