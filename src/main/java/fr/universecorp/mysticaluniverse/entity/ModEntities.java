package fr.universecorp.mysticaluniverse.entity;

import com.google.common.collect.ImmutableMap;
import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;

public class ModEntities {

    public static final EntityType<DruidEntity> DRUID_ENTITY =
            Registry.register(Registry.ENTITY_TYPE, new Identifier(MysticalUniverse.MODID, "druid"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, DruidEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build());
    public static ImmutableMap<Integer, TradeOffers.Factory[]> TRADE_LIST;

    public static void registerEntities() {

        TRADE_LIST = ImmutableMap.of(
                1, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(new ItemStack(ModItems.BOTTLE_OF_ETHER), 12, 4, 16, 4, 0.2f)},
                2, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(new ItemStack(ModItems.ETERIUM_INGOT), 8, 1, 12, 5, 0.2f)},
                3, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(new ItemStack(ModFluids.LIQUID_ETHER_BUCKET), 40, 1, 8, 24, 0.2f)}
        );

        FabricDefaultAttributeRegistry.register(DRUID_ENTITY, DruidEntity.setAttribute());
    }
}
