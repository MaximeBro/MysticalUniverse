package fr.universecorp.mysticaluniverse.world.feature;

import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {

    public static final List<OreFeatureConfig.Target> OVERWORLD_ERETIUM_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.ETERIUM_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ETERIUM_ORE.getDefaultState())
    );

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ETERIUM_ORE =
            ConfiguredFeatures.register("eterium_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_ERETIUM_ORES, 7));


    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> MYCELIUM_TREE =
            ConfiguredFeatures.register("mycelium_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.MYCELIUM_LOG),
                    new StraightTrunkPlacer(5, 1, 0),
                    BlockStateProvider.of(ModBlocks.MYCELIUM_LEAVES),
                    new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                    new TwoLayersFeatureSize(0, 0, 0)).ignoreVines().build());

    public static void registerConfiguredFeatures() {
        System.out.println("ModConfiguredFeatures DEBUG");
    }
}
