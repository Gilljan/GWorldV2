package de.gilljan.gworld.api;

/**
 * Main entry point for the GWorld API.
 * Provides access to all API functions.
 */
public interface GWorldAPI {

    /**
     * Gets the world manager, which is responsible for handling all manageable worlds.
     *
     * @return The {@link IWorldManager} instance.
     */
    IWorldManager getWorldManager();

    /**
     * Gets the current version of the GWorld plugin.
     *
     * @return A string representing the plugin version.
     */
    String getVersion();
}
