package de.gilljan.gworld.utils;

public class SecureWorldNameUtil {

    private SecureWorldNameUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isSecuredWorldName(String worldName) {
        return !worldName.matches("(?i)(.*\\..*|.*/.*|.*plugins.*|.*logs.*|.*old_maps*.)");
    }

}
