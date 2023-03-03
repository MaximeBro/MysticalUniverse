package fr.universecorp.mysticaluniverse.registry;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEFurnaceBlockEntity;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEWorkbenchBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<IEFurnaceBlockEntity> IEFURNACE;
    public static BlockEntityType<IEWorkbenchBlockEntity> IEWORKBENCH;

    public static BlockEntityType<IEComposterEntity> IECOMPOSTER;

    public static void registerBlockEntities() {
        IEFURNACE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MysticalUniverse.MODID, "iefurnace_smelting"),
                FabricBlockEntityTypeBuilder.create(IEFurnaceBlockEntity::new, ModBlocks.INFUSED_ETERIUM_FURNACE).build(null));

        IEWORKBENCH = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MysticalUniverse.MODID, "ieworkbench_crafting"),
                FabricBlockEntityTypeBuilder.create(IEWorkbenchBlockEntity::new, ModBlocks.INFUSED_ETERIUM_WORKBENCH).build(null));

        IECOMPOSTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MysticalUniverse.MODID, "iecomposter_composting"),
                FabricBlockEntityTypeBuilder.create(IEComposterEntity::new, ModBlocks.INFUSED_ETERIUM_COMPOSTER).build(null));

        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, IEFURNACE);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, IEWORKBENCH);
    }
}
