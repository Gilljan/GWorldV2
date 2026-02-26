package de.gilljan.gworld.placeholder;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.placeholder.provider.WorldAliasProvider;
import de.gilljan.gworld.placeholder.provider.WorldNameProvider;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class GWorldPlaceholderExpansion extends PlaceholderExpansion {

    private final GWorld plugin;
    private final List<GWorldPlaceholderProvider> providers = new ArrayList<>();

    public GWorldPlaceholderExpansion(GWorld plugin) {
        this.plugin = plugin;

        // Register static placeholders
        // in the future

        // Register dynamic placeholders
        providers.add(new WorldAliasProvider(plugin.getWorldManager()));
        providers.add(new WorldNameProvider(plugin.getWorldManager()));
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        return providers.stream()
                .filter(provider -> provider.matches(params))
                .findFirst()
                .map(provider -> provider.resolve(player, params))
                .orElse(null);
    }

    private void addStaticPlaceholder(String id, BiFunction<OfflinePlayer, String, String> resolver) {
        providers.add(new GWorldPlaceholderProvider() {
            @Override
            public boolean matches(String placeholder) {
                return placeholder.equalsIgnoreCase(id);
            }

            @Override
            public String resolve(OfflinePlayer player, String placeholder) {
                return resolver.apply(player, placeholder);
            }
        });
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().stream().reduce((a, b) -> a + ", " + b).orElse("Unknown");
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }
}
