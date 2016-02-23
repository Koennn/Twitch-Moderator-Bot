package me.koenn.tb.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

    public static String urlToString(String url) throws IOException {
        InputStream in = new URL(url).openStream();
        String response = null;
        try {
            response = IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return response;
    }

    public static Path getDataDir() {
        return Paths.get(System.getProperty("user.dir"), "data");
    }
}
