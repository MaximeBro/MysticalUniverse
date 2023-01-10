package fr.universecorp.mysticaluniverse.registry;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.fluids.LiquidEtherFluid;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModFluids {
    public static FlowableFluid STILL_LIQUID_ETHER;
    public static FlowableFluid FLOWING_LIQUID_ETHER;
    public static Block LIQUID_ETHER_BLOCK;
    public static Item LIQUID_ETHER_BUCKET;

    public static void register() {
        STILL_LIQUID_ETHER = Registry.register(Registry.FLUID,
                new Identifier(MysticalUniverse.MODID, "still_liquid_ether"), new LiquidEtherFluid.Still());
        FLOWING_LIQUID_ETHER = Registry.register(Registry.FLUID,
                new Identifier(MysticalUniverse.MODID, "flowing_liquid_ether"), new LiquidEtherFluid.Flowing());
        LIQUID_ETHER_BLOCK = Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "liquid_ether_block"),
                new FluidBlock(ModFluids.STILL_LIQUID_ETHER, FabricBlockSettings.copyOf(Blocks.WATER)){ });
        LIQUID_ETHER_BUCKET = Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "liquid_ether_bucket"),
                new BucketItem(ModFluids.STILL_LIQUID_ETHER, new FabricItemSettings().group(MysticalUniverse.MYSTICAL_GROUP).recipeRemainder(Items.BUCKET).maxCount(1)));
    }
}
