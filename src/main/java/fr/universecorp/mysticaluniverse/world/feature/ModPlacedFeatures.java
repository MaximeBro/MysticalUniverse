package fr.universecorp.mysticaluniverse.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryEntry<PlacedFeature> ERETIUM_ORE_PLACED = PlacedFeatures.register("eretium_ore_placed",
            ModConfiguredFeatures.ETERIUM_ORE, modifiersWithCount(7,
            HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.fixed(8))));


    public static final RegistryEntry<PlacedFeature> BLUE_CLEMATITE = PlacedFeatures.register("blue_clematite_placed",
            ModConfiguredFeatures.BLUE_CLEMATITE, RarityFilterPlacementModifier.of(4), SquarePlacementModifier.of(),
            PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of()
    );

    public static final RegistryEntry<PlacedFeature> MYCELIUM_TREE_PLACED = PlacedFeatures.register("mycelium_tree_placed",
            ModConfiguredFeatures.MYCELIUM_SPAWN,
            VegetationPlacedFeatures.modifiers(PlacedFeatures.createCountExtraModifier(1, 0.5f, 1))
    );

    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    private static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }
}
