package de.gilljan.gworld.listener;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GWorld.getInstance().getWorldManager().getWorld(player.getWorld().getName()).ifPresent(manageableWorld -> {
            if(WorldProperty.getValue(WorldProperty.DEFAULT_GAMEMODE, manageableWorld)) {
                player.setGameMode(WorldProperty.getValue(WorldProperty.GAMEMODE, manageableWorld));
            }
        });

        // Update notification
        if (!event.getPlayer().hasPermission("GWorld.updateNotification")) {
            return;
        }

        if (!GWorld.getInstance().getConfig().getBoolean("UpdateNotification", true)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(GWorld.getInstance(), () -> {
            try {
                URL url = new URL("https://gilljan.de/versions/GWorld/index.html");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String latestVersionStr = reader.readLine();
                    if (latestVersionStr == null || latestVersionStr.trim().isEmpty()) {
                        GWorld.getInstance().getLogger().warning("Could not read version from update server.");
                        return;
                    }

                    System.out.println("Latest version: " + latestVersionStr);

                    String currentVersionStr = GWorld.getInstance().getDescription().getVersion();
                    if (isNewerVersion(latestVersionStr.trim(), currentVersionStr)) {
                        String message = SendMessageUtil.sendMessage("UpdateNotification")
                                .replace("%version%", latestVersionStr.trim())
                                .replace("%link%", "https://gworld.gilljan.de");
                        event.getPlayer().sendMessage(message);
                    }
                } finally {
                    connection.disconnect();
                }
            } catch (IOException e) {
                GWorld.getInstance().getLogger().warning("Failed to check for updates: " + e.getMessage());
            }
        });
    }

    private boolean isNewerVersion(String latestVersion, String currentVersion) {
        try {
            String[] latestParts = latestVersion.split("\\.");
            String[] currentParts = currentVersion.split("\\.");

            int length = Math.max(latestParts.length, currentParts.length);
            for (int i = 0; i < length; i++) {
                int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
                int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;

                if (latestPart > currentPart) {
                    return true;
                }
                if (latestPart < currentPart) {
                    return false;
                }
            }
            return false;
        } catch (NumberFormatException e) {
            GWorld.getInstance().getLogger().warning("Could not parse version strings for update check.");
            return false;
        }
    }

}
