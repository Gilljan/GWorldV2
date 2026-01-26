package de.gilljan.gworld.api.gamerule;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.api.gamerule.paper.PaperGameRuleAdapter;
import de.gilljan.gworld.api.gamerule.spigot.SpigotGameRuleAdapter;

public class GameRuleAdapterFactory {
    public static GameRuleAdapter createAdapter() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            GWorld.getInstance().getLogger().info("Paper detected, using PaperGameRuleAdapter.");
            return new PaperGameRuleAdapter();
        } catch (ClassNotFoundException e) {
            GWorld.getInstance().getLogger().info("Paper not detected, using SpigotGameRuleAdapter.");
            return new SpigotGameRuleAdapter();
        }
    }
}
