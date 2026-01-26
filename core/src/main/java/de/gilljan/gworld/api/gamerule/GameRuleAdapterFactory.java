package de.gilljan.gworld.api.gamerule;

import de.gilljan.gworld.utils.ServerAndVersionUtil;

public class GameRuleAdapterFactory {
    public static GameRuleAdapter createAdapter() {
        boolean isPaper = ServerAndVersionUtil.isPaperServer();
        boolean is1_21_11OrNewer = ServerAndVersionUtil.is1_21_11OrNewer();

        String adapterPath = "de.gilljan.gworld.%version%.api.gamerule.%adapter%";

        try {
            String className = adapterPath
                    .replace("%version%", is1_21_11OrNewer ? "v1_21_11" : "v1_20")
                    .replace("%adapter%", ((isPaper ? "Paper" : "Spigot") + (is1_21_11OrNewer ? "1_21_11" : "1_20Up") + "GameRuleAdapter"));
            System.out.println(className);
            Class<?> adapterClass = Class.forName(className);
            return (GameRuleAdapter) adapterClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create GameRuleAdapter", e);
        }
    }
}
