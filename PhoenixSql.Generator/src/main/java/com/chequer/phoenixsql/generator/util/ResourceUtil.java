package com.chequer.phoenixsql.generator.util;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("UnstableApiUsage")
public class ResourceUtil {
    public static String readString(String resourceName) {
        var url = Resources.getResource(resourceName);

        try {
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
