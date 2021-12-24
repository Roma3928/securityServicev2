package ru.sfedu.securityservice.utils;

import ru.sfedu.securityservice.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration utility. Allows to get configuration properties from the
 * default configuration file
 *
 * @author Boris Jmailov
 */
public class ConfigurationUtil {

    private static final String CONFIG_PATH = System.getProperty(Constants.ENV_PROP_KEY, Constants.ENV_PROP_VALUE);

    private static final Properties configuration = new Properties();
    /**
     * Hides default constructor
     */
    public ConfigurationUtil() {
    }

    private static Properties getConfiguration() throws IOException {
        if(configuration.isEmpty()){
            loadConfiguration();
        }
        return configuration;
    }

    /**
     * Loads configuration from <code>DEFAULT_CONFIG_PATH</code>
     * @throws IOException In case of the configuration file read failure
     */
    private static void loadConfiguration() throws IOException {
        File nf = new File(CONFIG_PATH);
//        System.out.println(nf);
        try (InputStream in = new FileInputStream(nf) // DEFAULT_CONFIG_PATH.getClass().getResourceAsStream(DEFAULT_CONFIG_PATH);
        ) {
            configuration.load(in);
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Gets configuration entry value
     * @param key Entry key
     * @return Entry value by key
     * @throws IOException In case of the configuration file read failure
     */
    public static String getConfigurationEntry(String key) throws IOException{
        return getConfiguration().getProperty(key);
    }

}
