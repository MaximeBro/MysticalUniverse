package fr.universecorp.mysticaluniverse.registry;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.blocks.ChargedEteriumBlock;
import fr.universecorp.mysticaluniverse.custom.blocks.IEFurnaceBlock;
import fr.universecorp.mysticaluniverse.world.feature.tree.MyceliumSaplingGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModBlocks {
    public static final Block ETERIUM_BLOCK = new Block(FabricBlockSettings
            .of(Material.METAL)
            .strength(4.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)
    );

    public static final Block ETERIUM_ORE = new OreBlock(FabricBlockSettings
            .of(Material.STONE)
            .strength(4.0f)
            .requiresTool()
            .luminance(5)
            .sounds(BlockSoundGroup.STONE)
    );

    public static final Block CHARGED_ETERIUM_BLOCK = new ChargedEteriumBlock(FabricBlockSettings
            .of(Material.METAL)
            .strength(4.0f)
            .requiresTool()
            .luminance(10)
            .sounds(BlockSoundGroup.METAL)
    );

    public static final Block REFINED_ETERIUM_BLOCK = new Block(FabricBlockSettings
            .of(Material.METAL)
            .strength(4.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)
    );

    public static final Block INFESTED_ETERIUM_BLOCK = new Block(FabricBlockSettings
            .of(Material.METAL)
            .strength(4.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)
    );

    public static final Block INFUSED_ETERIUM_FURNACE = new IEFurnaceBlock(FabricBlockSettings
            .of(Material.METAL)
            .strength(3.5f)
            .requiresTool()
            .nonOpaque()
    );

    public static final Block ETERIUM_CORE_BLOCK = new Block(FabricBlockSettings
            .of(Material.METAL)
            .strength(4.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)
    );

    public static final Block INFUSED_CORE_BLOCK = new Block(FabricBlockSettings
            .of(Material.METAL)
            .strength(4.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)
    );


    public static final Block MYCELIUM_LOG = new PillarBlock(FabricBlockSettings
            .copy(Blocks.OAK_LOG)
    );

    public static final Block MYCELIUM_PLANKS = new Block(FabricBlockSettings
            .copy(Blocks.OAK_PLANKS)
    );

    public static final Block MYCELIUM_LEAVES = new LeavesBlock(FabricBlockSettings
            .copy(Blocks.OAK_LEAVES)
    );

    public static final Block MYCELIUM_SAPLING = new SaplingBlock(new MyceliumSaplingGenerator(),
            FabricBlockSettings.copy(Blocks.OAK_SAPLING)
    );


    public static final Block BLUE_CLEMATITE = new FlowerBlock(
            StatusEffects.SATURATION, 7,
            FabricBlockSettings.copy(Blocks.DANDELION)
    );



    public static void registerAll() {
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "eterium_block"), ETERIUM_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "eterium_ore"), ETERIUM_ORE);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "charged_eterium_block"), CHARGED_ETERIUM_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "refined_eterium_block"), REFINED_ETERIUM_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "infested_eterium_block"), INFESTED_ETERIUM_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "infused_eterium_furnace"), INFUSED_ETERIUM_FURNACE);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "eterium_core_block"), ETERIUM_CORE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "infused_core_block"), INFUSED_CORE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "mycelium_log"), MYCELIUM_LOG);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "mycelium_planks"), MYCELIUM_PLANKS);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "mycelium_leaves"), MYCELIUM_LEAVES);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "mycelium_sapling"), MYCELIUM_SAPLING);
        Registry.register(Registry.BLOCK, new Identifier(MysticalUniverse.MODID, "blue_clematite"), BLUE_CLEMATITE);
    }
}
