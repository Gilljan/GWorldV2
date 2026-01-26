package de.gilljan.gworld.api.gamerule;

import org.bukkit.World;

public interface GameRuleAdapter {
    <T> void setGameRule(World world, GGameRule gameRule, T value);

    <T> T getGameRuleValue(World world, GGameRule gameRule, Class<T> valueType);
}
