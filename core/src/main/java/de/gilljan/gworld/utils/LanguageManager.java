package de.gilljan.gworld.utils;

import de.gilljan.gworld.GWorld;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LanguageManager {

    private YamlConfiguration diskConfig;
    private YamlConfiguration fallbackConfig;
    private final String defaultLang = "en_EN";

    public void load() {
        String langCode = GWorld.getInstance().getConfig().getString("Language");
        if (langCode == null) langCode = defaultLang;

        if(langCode.equalsIgnoreCase("de")) {
            langCode = "de_DE";
        } else {
            langCode = "en_EN";
        }

        String fileName = langCode + ".yml";
        File file = new File(GWorld.getInstance().getDataFolder(), fileName);

        if (!file.exists()) {
            if (GWorld.getInstance().getResource(fileName) != null) {
                GWorld.getInstance().saveResource(fileName, false);
            } else {
                GWorld.getInstance().getLogger().warning("Language " + langCode + " not found. Load English as fallback.");
                fileName = "en_EN.yml";
                file = new File(GWorld.getInstance().getDataFolder(), fileName);
                if (!file.exists()) GWorld.getInstance().saveResource(fileName, false);
            }
        }

        diskConfig = YamlConfiguration.loadConfiguration(file);

        InputStream stream = GWorld.getInstance().getResource(fileName);
        if (stream == null) {
            stream = GWorld.getInstance().getResource(defaultLang + ".yml");
        }

        if (stream != null) {
            fallbackConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
        }
    }

    public String getMessage(String key) {
        String message = null;

        if (diskConfig.contains(key)) {
            message = diskConfig.getString(key);
        }
        else if (fallbackConfig != null && fallbackConfig.contains(key)) {
            message = fallbackConfig.getString(key);
            GWorld.getInstance().getLogger().warning("Missing translation for key '" + key + "' in language file. Using fallback. Please regenerate your language files to include missing keys.");
        }

        if (message == null) {
            return "§cMISSING_LANG: " + key;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessageWithPrefix(String key) {
        String prefix = getMessage("Prefix");
        return prefix + getMessage(key);
    }
}
