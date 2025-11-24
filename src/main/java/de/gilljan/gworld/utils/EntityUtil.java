package de.gilljan.gworld.utils;

import de.gilljan.gworld.GWorld;
import org.bukkit.entity.*;

public class EntityUtil {

    public static void getAllEntities() {
        for(EntityType entityType : EntityType.values()) {
            if(!entityType.isAlive()) {
                continue;
            }

            Class<? extends Entity> entity = entityType.getEntityClass();

            assert entity != null;

            try {
                if(Monster.class.isAssignableFrom(entity) || EnderDragon.class.isAssignableFrom(entity)
                        || Slime.class.isAssignableFrom(entity) || Ghast.class.isAssignableFrom(entity)
                        || Class.forName("org.bukkit.entity.Shulker").isAssignableFrom(entity)
                        || Class.forName("org.bukkit.entity.Phantom").isAssignableFrom(entity)
                        || Class.forName("org.bukkit.entity.Hoglin").isAssignableFrom(entity)) {
                    GWorld.MONSTER.add("Craft" + entity.getSimpleName());
                    continue;
                }

            } catch (ClassNotFoundException ignored) {
                //ignored
            }

            GWorld.ANIMALS.add("Craft" + entity.getSimpleName());
        }

        GWorld.ANIMALS.remove("Player");
        GWorld.ANIMALS.remove("ArmorStand");
    }

}
