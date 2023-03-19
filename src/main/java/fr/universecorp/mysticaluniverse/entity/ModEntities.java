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
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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

    public static final TrackedDataHandler<DruidState> DRUID_STATE = TrackedDataHandler.ofEnum(DruidState.class);

    public static ImmutableMap<Integer, TradeOffers.Factory[]> TRADE_LIST;

    public static void registerEntities() {

        TRADE_LIST = ImmutableMap.of(
                1, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(new ItemStack(ModItems.BOTTLE_OF_ETHER), 12, 4, 16, 4, 0.2f), new TradeOffers.BuyForOneEmeraldFactory(ModItems.BLUE_CLEMATITE_ESSENCE, 4, 8, 7)},
                2, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(new ItemStack(ModItems.ETERIUM_INGOT), 8, 1, 12, 10, 0.2f)},
                3, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(new ItemStack(ModFluids.LIQUID_ETHER_BUCKET), 40, 1, 8, 32, 0.5f)},
                4, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(new ItemStack(ModItems.BILLHOOK), 24, 1, 1, 65, 0.8f)}
        );

        TrackedDataHandlerRegistry.register(DRUID_STATE);

        FabricDefaultAttributeRegistry.register(DRUID_ENTITY, DruidEntity.setAttribute());
    }
}
