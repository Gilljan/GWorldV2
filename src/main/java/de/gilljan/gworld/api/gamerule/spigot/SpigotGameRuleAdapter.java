package de.gilljan.gworld.api.gamerule.spigot;

import de.gilljan.gworld.api.gamerule.GGameRule;
import de.gilljan.gworld.api.gamerule.GameRuleAdapter;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;

public class SpigotGameRuleAdapter implements GameRuleAdapter {
    @Override
    public <T> void setGameRule(World world, GGameRule gameRule, T value) {
        NamespacedKey key = NamespacedKey.fromString(gameRule.getGameRuleName());
        GameRule<T> bukkitGameRule = (GameRule<T>) Registry.GAME_RULE.get(key);

        if (bukkitGameRule != null && gameRule.getValueType().isInstance(value)) {
            world.setGameRule(bukkitGameRule, value);
        }
    }

    @Override
    public <T> T getGameRuleValue(World world, GGameRule gameRule, Class<T> valueType) {
        NamespacedKey key = NamespacedKey.fromString(gameRule.getGameRuleName());
        GameRule<T> bukkitGameRule = (GameRule<T>) Registry.GAME_RULE.get(key);

        if (bukkitGameRule != null && gameRule.getValueType().isInstance(valueType)) {
            return world.getGameRuleValue(bukkitGameRule);
        }

        return null;
    }
}
