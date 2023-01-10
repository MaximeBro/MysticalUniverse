package fr.universecorp.mysticaluniverse.world.feature;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;

import java.util.List;

public class ModConfiguredFeatures {

    public static final List<OreFeatureConfig.Target> OVERWORLD_ERETIUM_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.ETERIUM_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ETERIUM_ORE.getDefaultState())
    );

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ETERIUM_ORE =
            ConfiguredFeatures.register("eterium_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_ERETIUM_ORES, 7));


    public static void registerConfiguredFeatures() {
        System.out.println("ModConfiguredFeatures DEBUG");
    }
}
