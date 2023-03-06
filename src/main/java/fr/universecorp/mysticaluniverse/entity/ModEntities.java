package fr.universecorp.mysticaluniverse.entity;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {

    public static EntityType<DruidEntity> DRUID_ENTITY;

    public static void registerEntities() {
        DRUID_ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier(MysticalUniverse.MODID, "druid_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DruidEntity::new)
                                   .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                   .build());

        FabricDefaultAttributeRegistry.register(DRUID_ENTITY, DruidEntity.createMobAttributes());
    }
}
