package fr.universecorp.mysticaluniverse;

import fr.universecorp.mysticaluniverse.client.MysticalTab;
import fr.universecorp.mysticaluniverse.entity.ModEntities;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.custom.recipe.ModRecipes;
import fr.universecorp.mysticaluniverse.registry.ModScreenHandlers;
import fr.universecorp.mysticaluniverse.registry.*;
import fr.universecorp.mysticaluniverse.world.feature.ModConfiguredFeatures;
import fr.universecorp.mysticaluniverse.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;


public class MysticalUniverse implements ModInitializer {

    public static final String MODID = "mysticaluniverse";

    public static final MysticalTab MYSTICAL_GROUP = MysticalTab.MysticalTabBuilder.create(
            new Identifier(MysticalUniverse.MODID, "mysticalgroup"))
            .key("mysticalgroup")
            .icon(ModItems.ICON.getDefaultStack())
            .appendItems(stacks -> {
                stacks.add(new ItemStack(ModBlocks.ETERIUM_ORE));
                stacks.add(new ItemStack(ModBlocks.ETERIUM_BLOCK));
                stacks.add(new ItemStack(ModItems.ETERIUM_INGOT));
                stacks.add(new ItemStack(ModItems.ETERIUM_DUST));
                stacks.add(new ItemStack(ModItems.CHARGED_ETERIUM_BLOCK));
                stacks.add(new ItemStack(ModItems.CHARGED_ETERIUM_INGOT));
                stacks.add(new ItemStack(ModItems.ETERIUM_COAL));
                stacks.add(new ItemStack(ModFluids.LIQUID_ETHER_BUCKET));
                stacks.add(new ItemStack(ModBlocks.INFUSED_ETERIUM_FURNACE));
                stacks.add(new ItemStack(ModBlocks.INFUSED_ETERIUM_WORKBENCH));
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
                stacks.add(new ItemStack(ModItems.ETHER_LILY));
                stacks.add(new ItemStack(ModItems.INFUSED_LILY));
                stacks.add(new ItemStack(ModItems.BOTTLE_OF_ETHER));
                stacks.add(new ItemStack(ModItems.DRUID_SPAWN_EGG));
                stacks.add(new ItemStack(ModItems.ETERIUM_ARMOR_CORE));
                stacks.add(new ItemStack(ModItems.INFUSED_ARMOR_CORE));
                stacks.add(new ItemStack(ModItems.INFUSED_ETERIUM_COMPOSTER));
                stacks.add(new ItemStack(ModItems.INFUSED_ETERIUM_HELMET));
                stacks.add(new ItemStack(ModItems.INFUSED_ETERIUM_CHESTPLATE));
                stacks.add(new ItemStack(ModItems.INFUSED_ETERIUM_LEGGINGS));
                stacks.add(new ItemStack(ModItems.INFUSED_ETERIUM_BOOTS));
            })
            .build();


    @Override
    public void onInitialize() {
        ModConfiguredFeatures.registerConfiguredFeatures();
        ModWorldGen.generateWorldGen();

        ModItems.registerAll();
        ModBlocks.registerAll();
        ModFluids.register();
        ModBlockEntities.registerBlockEntities();
        ModFlammableBlocks.registerFlammableBlocks();

        ModEntities.registerEntities();

        ModScreenHandlers.registerAllScreenHandlers();
        ModRecipes.registerRecipes();

        FuelRegistry.INSTANCE.add(ModItems.ETERIUM_COAL, 2400);
    }
}
