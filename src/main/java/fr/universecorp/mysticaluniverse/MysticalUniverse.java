package fr.universecorp.mysticaluniverse;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.ModBlockEntities;
import fr.universecorp.mysticaluniverse.custom.recipe.ModRecipes;
import fr.universecorp.mysticaluniverse.custom.screen.ModScreenHandlers;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.registry.ModFlammableBlocks;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import fr.universecorp.mysticaluniverse.world.feature.ModConfiguredFeatures;
import fr.universecorp.mysticaluniverse.world.gen.ModFlowerGeneration;
import fr.universecorp.mysticaluniverse.world.gen.ModOreGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;


public class MysticalUniverse implements ModInitializer {

    public static final String MODID = "mysticaluniverse";

    public static final ItemGroup MYSTICAL_GROUP = FabricItemGroupBuilder.create(
            new Identifier(MODID, "mysticalgroup"))
            .icon(() -> new ItemStack(ModItems.CHARGED_ETERIUM_INGOT))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(ModBlocks.ETERIUM_ORE));
                stacks.add(new ItemStack(ModBlocks.ETERIUM_BLOCK));
                stacks.add(new ItemStack(ModItems.ETERIUM_INGOT));
                stacks.add(new ItemStack(ModItems.ETERIUM_DUST));
                stacks.add(new ItemStack(ModBlocks.INFESTED_ETERIUM_BLOCK));
                stacks.add(new ItemStack(ModItems.CHARGED_ETERIUM_BLOCK));
                stacks.add(new ItemStack(ModItems.CHARGED_ETERIUM_INGOT));
                stacks.add(new ItemStack(ModItems.ETERIUM_COAL));
                stacks.add(new ItemStack(ModFluids.LIQUID_ETHER_BUCKET));
                stacks.add(new ItemStack(ModBlocks.INFUSED_ETERIUM_FURNACE));
                stacks.add(new ItemStack(ModBlocks.INFUSED_ETERIUM_WORKBENCH));
                stacks.add(new ItemStack(ModBlocks.REFINED_ETERIUM_BLOCK));
                stacks.add(new ItemStack(ModItems.MYCELIUM_PESTLE));
                stacks.add(new ItemStack(ModItems.MYCELIUM_MORTAR));
                stacks.add(new ItemStack(ModItems.MORTAR_AND_PESTLE));
                stacks.add(new ItemStack(ModItems.ETERIUM_CORE_BLOCK));
                stacks.add(new ItemStack(ModItems.INFUSED_CORE_BLOCK));
                stacks.add(new ItemStack(ModItems.MYCELIUM_LOG));
                stacks.add(new ItemStack(ModItems.MYCELIUM_PLANKS));
                stacks.add(new ItemStack(ModItems.MYCELIUM_LEAVES));
                stacks.add(new ItemStack(ModItems.MYCELIUM_SAPLING));
                stacks.add(new ItemStack(ModItems.MYCELIUM_STICK));
                stacks.add(new ItemStack(ModItems.BLUE_CLEMATITE));
                stacks.add(new ItemStack(ModItems.BLUE_CLEMATITE_ESSENCE));
                stacks.add(new ItemStack(ModItems.BOTTLE_OF_ETHER));
            })
            .build();


    @Override
    public void onInitialize() {
        ModConfiguredFeatures.registerConfiguredFeatures();

        ModItems.registerAll();
        ModBlocks.registerAll();
        ModFluids.register();
        ModBlockEntities.registerBlockEntities();
        ModFlammableBlocks.registerFlammableBlocks();
        ModOreGeneration.generateOres();
        ModFlowerGeneration.generateFlowers();
        ModScreenHandlers.registerAllScreenHandlers();
        ModRecipes.registerRecipes();

        FuelRegistry.INSTANCE.add(ModItems.ETERIUM_COAL, 2400);

    }
}
