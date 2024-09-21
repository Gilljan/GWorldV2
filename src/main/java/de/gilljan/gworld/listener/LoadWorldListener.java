package de.gilljan.gworld.listener;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.SendMessageUtil;
import de.gilljan.gworld.world.ManageableWorld;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

public class LoadWorldListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        //wenn kein automatisches Importieren aktiviert ist, wird die Methode beendet todo
        if(!GWorld.autoImport) {
            return;
        }

        //wenn gworld die welt kennt, dann nicht laden
        if(GWorld.getInstance().getDataHandler().containsWorld(event.getWorld().getName())) {
            return;
        }

        //Welt aufnehmen und laden

        String generator = null;
        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            ChunkGenerator plC = pl.getDefaultWorldGenerator("", "");
            if (plC != null && pl.isEnabled() && event.getWorld().getGenerator() != null) {
                if (plC.getClass().equals(event.getWorld().getGenerator().getClass())) {
                    generator = pl.getName();
                }
            }
        }


        WorldData data = new WorldData(event.getWorld().getName(), event.getWorld().getEnvironment(), event.getWorld().getWorldType(), event.getWorld().getSeed(), generator);

        GWorld.getInstance().getWorldManager().addWorld(data).setAllProperties();

        //todo inform players when permission is given
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("gworld.notify")) {
                player.sendMessage(SendMessageUtil.sendMessage("AutoImport").replaceAll("%world%", event.getWorld().getName()));
            }
        }

    }

}
