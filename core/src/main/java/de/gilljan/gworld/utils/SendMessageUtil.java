package de.gilljan.gworld.utils;

import de.gilljan.gworld.GWorld;

public class SendMessageUtil {

    private SendMessageUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String sendMessage(String key) {
        LanguageManager langMan = GWorld.getInstance().getLanguageManager();

        String message = langMan.getMessage(key);

        return message.replace("%prefix%", GWorld.prefix);
    }

    public static String sendRawMessage(String message) {
        return message
                .replace("&0", "§0")
                .replace("&1", "§1")
                .replace("&2", "§2")
                .replace("&3", "§3")
                .replace("&4", "§4")
                .replace("&5", "§5")
                .replace("&6", "§6")
                .replace("&7", "§7")
                .replace("&8", "§8")
                .replace("&9", "§9")
                .replace("&a", "§a")
                .replace("&b", "§b")
                .replace("&c", "§c")
                .replace("&d", "§d")
                .replace("&e", "§e")
                .replace("&f", "§f")
                .replace("&l", "§l")
                .replace("&m", "§m")
                .replace("&n", "§n")
                .replace("&o", "§o")
                .replace("&u", "§u")
                .replace("&r", "§r")
                .replace("&k", "§k");
    }

}
