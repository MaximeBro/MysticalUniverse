package fr.universecorp.mysticaluniverse.registry;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.items.*;
import fr.universecorp.mysticaluniverse.custom.items.materials.ModArmorMaterials;
import fr.universecorp.mysticaluniverse.custom.items.materials.ModToolMaterials;
import fr.universecorp.mysticaluniverse.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static fr.universecorp.mysticaluniverse.MysticalUniverse.MYSTICAL_GROUP;

public class ModItems {

    // Icon
    public static final Item ICON = new Item(new FabricItemSettings());


    // Items
    public static final Item CHARGED_ETERIUM_INGOT = new ChargedEteriumIngot(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item ETERIUM_INGOT = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item ETERIUM_DUST = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item MYCELIUM_STICK = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item ETERIUM_COAL = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item MYCELIUM_PESTLE = new Item(new FabricItemSettings().maxCount(1).group(MYSTICAL_GROUP));
    public static final Item MYCELIUM_MORTAR = new Item(new FabricItemSettings().maxCount(1).group(MYSTICAL_GROUP));
    public static final Item MORTAR_AND_PESTLE = new MortarAndPestle(new FabricItemSettings().maxCount(1).group(MYSTICAL_GROUP));
    public static final Item BOTTLE_OF_ETHER = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item BLUE_CLEMATITE_ESSENCE = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item ETERIUM_ARMOR_CORE = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item INFUSED_ARMOR_CORE = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item BILLHOOK = new Billhook(0.0f, -3.0f, ModToolMaterials.BILLHOOK, new FabricItemSettings().maxCount(1).group(MYSTICAL_GROUP));
    public static final Item DRUID_SPAWN_EGG = new SpawnEggItem(ModEntities.DRUID_ENTITY, 0x1F7B1F, 0xF5F2E9,
            new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item INFUSED_ETERIUM_HELMET = new ModArmorItem(ModArmorMaterials.ETERIUM, EquipmentSlot.HEAD,
            new FabricItemSettings().group(MYSTICAL_GROUP).fireproof());
    public static final Item INFUSED_ETERIUM_CHESTPLATE = new ModArmorItem(ModArmorMaterials.ETERIUM, EquipmentSlot.CHEST,
            new FabricItemSettings().group(MYSTICAL_GROUP).fireproof());
    public static final Item INFUSED_ETERIUM_LEGGINGS = new ModArmorItem(ModArmorMaterials.ETERIUM, EquipmentSlot.LEGS,
            new FabricItemSettings().group(MYSTICAL_GROUP).fireproof());
    public static final Item INFUSED_ETERIUM_BOOTS = new ModArmorItem(ModArmorMaterials.ETERIUM, EquipmentSlot.FEET,
            new FabricItemSettings().group(MYSTICAL_GROUP).fireproof());

    // BlockItems
    public static final BlockItem ETERIUM_BLOCK = new BlockItem(ModBlocks.ETERIUM_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem ETERIUM_ORE = new BlockItem(ModBlocks.ETERIUM_ORE, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem CHARGED_ETERIUM_BLOCK = new BlockItem(ModBlocks.CHARGED_ETERIUM_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem INFUSED_ETERIUM_FURNACE = new BlockItem(ModBlocks.INFUSED_ETERIUM_FURNACE, new FabricItemSettings().maxCount(1).group(MYSTICAL_GROUP));
    public static final BlockItem INFUSED_ETERIUM_WORKBENCH = new BlockItem(ModBlocks.INFUSED_ETERIUM_WORKBENCH, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem ETERIUM_CORE_BLOCK = new BlockItem(ModBlocks.ETERIUM_CORE_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem INFUSED_CORE_BLOCK = new BlockItem(ModBlocks.INFUSED_CORE_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem MYCELIUM_LOG = new BlockItem(ModBlocks.MYCELIUM_LOG, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem MYCELIUM_PLANKS = new BlockItem(ModBlocks.MYCELIUM_PLANKS, new FabricItemSettings().group(MYSTICAL_GROUP));
    public static final BlockItem MYCELIUM_LEAVES = new BlockItem(ModBlocks.MYCELIUM_LEAVES, new FabricItemSettings().group(MYSTICAL_GROUP));
    public static final BlockItem MYCELIUM_SAPLING = new BlockItem(ModBlocks.MYCELIUM_SAPLING, new FabricItemSettings().group(MYSTICAL_GROUP));
    public static final BlockItem BLUE_CLEMATITE = new BlockItem(ModBlocks.BLUE_CLEMATITE, new FabricItemSettings().group(MYSTICAL_GROUP));
    public static final BlockItem ETHER_LILY = new BlockItem(ModBlocks.ETHER_LILY, new FabricItemSettings().group(MYSTICAL_GROUP));
    public static final BlockItem INFUSED_LILY = new BlockItem(ModBlocks.INFUSED_LILY, new FabricItemSettings().group(MYSTICAL_GROUP));
    public static final BlockItem INFUSED_ETERIUM_COMPOSTER = new BlockItem(ModBlocks.INFUSED_ETERIUM_COMPOSTER, new FabricItemSettings().group(MYSTICAL_GROUP));

    public static void registerAll() {

        //Items
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "charged_eterium_ingot"), CHARGED_ETERIUM_INGOT);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_ingot"), ETERIUM_INGOT);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_dust"), ETERIUM_DUST);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mycelium_stick"), MYCELIUM_STICK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_coal"), ETERIUM_COAL);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mycelium_pestle"), MYCELIUM_PESTLE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mycelium_mortar"), MYCELIUM_MORTAR);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mortar_and_pestle"), MORTAR_AND_PESTLE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "bottle_of_ether"), BOTTLE_OF_ETHER);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "blue_clematite_essence"), BLUE_CLEMATITE_ESSENCE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_armor_core"), ETERIUM_ARMOR_CORE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_armor_core"), INFUSED_ARMOR_CORE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "druid_spawn_egg"), DRUID_SPAWN_EGG);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_helmet"), INFUSED_ETERIUM_HELMET);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_chestplate"), INFUSED_ETERIUM_CHESTPLATE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_leggings"), INFUSED_ETERIUM_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_boots"), INFUSED_ETERIUM_BOOTS);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "billhook"), BILLHOOK);

        // Icon
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "icon"), ICON);

        //BlockItems
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_block"), ETERIUM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_ore"), ETERIUM_ORE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "charged_eterium_block"), CHARGED_ETERIUM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_furnace"), INFUSED_ETERIUM_FURNACE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_workbench"), INFUSED_ETERIUM_WORKBENCH);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_core_block"), ETERIUM_CORE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_core_block"), INFUSED_CORE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mycelium_log"), MYCELIUM_LOG);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mycelium_planks"), MYCELIUM_PLANKS);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mycelium_leaves"), MYCELIUM_LEAVES);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "mycelium_sapling"), MYCELIUM_SAPLING);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "blue_clematite"), BLUE_CLEMATITE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "ether_lily"), ETHER_LILY);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_lily"), INFUSED_LILY);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_composter"), INFUSED_ETERIUM_COMPOSTER);
    }
}
