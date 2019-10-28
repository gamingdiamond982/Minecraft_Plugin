package org.nologic.oisin.minecraftdiscord;

import java.util.*;
import java.util.Properties;

public class Configuration
{
    Properties config;

    public Configuration() {
        // TODO: read config from configuration file
    }

    public String getProperty(String key) {
        return System.getenv(key);
    }
}