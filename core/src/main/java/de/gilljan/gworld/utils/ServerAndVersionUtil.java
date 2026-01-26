package de.gilljan.gworld.utils;

public class ServerAndVersionUtil {
    private ServerAndVersionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isPaperServer() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String getServerVersion() {
        String version = org.bukkit.Bukkit.getBukkitVersion();
        if (version.contains("-")) {
            version = version.split("-")[0];
        }
        return version;
    }

    public static boolean is1_21_11OrNewer() {
        String version = getServerVersion();
        String[] parts = version.split("\\.");
        if (parts.length < 3) {
            return false;
        }
        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);

            if (major > 1) {
                return true;
            } else if (major == 1) {
                if (minor > 21) {
                    return true;
                } else if (minor == 21) {
                    return patch >= 11;
                }
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
