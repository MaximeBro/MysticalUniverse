package fr.universecorp.mysticaluniverse.registry;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.items.ChargedEteriumIngot;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static fr.universecorp.mysticaluniverse.MysticalUniverse.MYSTICAL_GROUP;

public class ModItems {

    // Items
    public static final Item CHARGED_ETERIUM_INGOT = new ChargedEteriumIngot(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item ETERIUM_INGOT = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item ETERIUM_STICK = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final Item ETERIUM_COAL = new Item(new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));


    // BlockItems
    public static final BlockItem ETERIUM_BLOCK = new BlockItem(ModBlocks.ETERIUM_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem ETERIUM_ORE = new BlockItem(ModBlocks.ETERIUM_ORE, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem CHARGED_ETERIUM_BLOCK = new BlockItem(ModBlocks.CHARGED_ETERIUM_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem REFINED_ETERIUM_BLOCK = new BlockItem(ModBlocks.REFINED_ETERIUM_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem INFESTED_ETERIUM_BLOCK = new BlockItem(ModBlocks.INFESTED_ETERIUM_BLOCK, new FabricItemSettings().maxCount(64).group(MYSTICAL_GROUP));
    public static final BlockItem INFUSED_ETERIUM_FURNACE = new BlockItem(ModBlocks.INFUSED_ETERIUM_FURNACE, new FabricItemSettings().maxCount(1).group(MYSTICAL_GROUP));



    public static void registerAll() {

        //Items
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "charged_eterium_ingot"), CHARGED_ETERIUM_INGOT);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_ingot"), ETERIUM_INGOT);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_stick"), ETERIUM_STICK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_coal"), ETERIUM_COAL);

        //BlockItems
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_block"), ETERIUM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "eterium_ore"), ETERIUM_ORE);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "charged_eterium_block"), CHARGED_ETERIUM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "refined_eterium_block"), REFINED_ETERIUM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infested_eterium_block"), INFESTED_ETERIUM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MysticalUniverse.MODID, "infused_eterium_furnace"), INFUSED_ETERIUM_FURNACE);
    }
}
