package de.gilljan.gworld.v1_20.api.gamerule;

import de.gilljan.gworld.api.gamerule.GGameRule;
import de.gilljan.gworld.api.gamerule.GameRuleAdapter;
import org.bukkit.GameRule;
import org.bukkit.World;

public class Paper1_20UpGameRuleAdapter implements GameRuleAdapter {
    @Override
    public <T> void setGameRule(World world, GGameRule gameRule, T value) {
        GameRule<T> bukkitGameRule = (GameRule<T>) GameRule.getByName(gameRule.getV20UpGameRuleName());

        if (bukkitGameRule != null && gameRule.getValueType().isInstance(value)) {
            world.setGameRule(bukkitGameRule, value);
        }
    }

    @Override
    public <T> T getGameRuleValue(World world, GGameRule gameRule, Class<T> valueType) {
        GameRule<T> bukkitGameRule = (GameRule<T>) GameRule.getByName(gameRule.getV20UpGameRuleName());

        if (bukkitGameRule != null && gameRule.getValueType().isInstance(valueType)) {
            return world.getGameRuleValue(bukkitGameRule);
        }

        return null;
    }
}
