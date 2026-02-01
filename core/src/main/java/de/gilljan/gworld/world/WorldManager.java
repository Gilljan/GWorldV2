package de.gilljan.gworld.world;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.api.IManageableWorld;
import de.gilljan.gworld.api.IWorldManager;
import de.gilljan.gworld.api.WorldCreationBuilder;
import de.gilljan.gworld.data.properties.WorldProperty;
import de.gilljan.gworld.data.world.WorldData;
import org.bukkit.World;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WorldManager implements IWorldManager {

    public ManageableWorld addWorld(WorldData world) {
        ManageableWorld manageableWorld = new ManageableWorld(world);
        GWorld.getInstance().getDataHandler().addWorld(world);

        return manageableWorld;
    }

    @Override
    public Optional<IManageableWorld> getWorld(String name) {
        return GWorld.getInstance().getDataHandler().getWorld(name).map(ManageableWorld::new);
    }

    @Override
    public List<IManageableWorld> getWorlds() {
        return GWorld.getInstance().getDataHandler().getWorlds().values().stream()
                .map(ManageableWorld::new)
                .map(IManageableWorld.class::cast)
                .toList();
    }

    @Override
    public IManageableWorld addWorldFromBuilder(WorldCreationBuilder builder) {
        WorldData worldData = new WorldData(builder.getName(), builder.getWorldType().getEnvironment(), builder.getWorldType().getWorldType(), builder.getSeed(), builder.getGenerator());
        ManageableWorld manageableWorld = new ManageableWorld(worldData);
        builder.getProperties().forEach((property, value) -> setWorldProperty(manageableWorld, property, value));
        GWorld.getInstance().getDataHandler().addWorld(worldData);
        return manageableWorld;
    }

    @SuppressWarnings("unchecked")
    private <T> void setWorldProperty(IManageableWorld world, WorldProperty<T> property, Object value) {
        WorldProperty.setValue(property, world, (T) value);
    }


    public void removeWorld(WorldData data) {
        GWorld.getInstance().getDataHandler().removeWorld(data);
    }

    @Override
    public boolean removeWorld(IManageableWorld world) {
        return world != null && world.deleteMap();
    }
}
