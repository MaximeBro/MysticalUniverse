package fr.universecorp.mysticaluniverse.custom.blocks.entity;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<IEFurnaceBlockEntity> IEFURNACE;
    public static BlockEntityType<IEWorkbenchBlockEntity> IEWORKBENCH;

    public static void registerBlockEntities() {
        IEFURNACE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MysticalUniverse.MODID, "iefurnace_smelting"),
                FabricBlockEntityTypeBuilder.create(IEFurnaceBlockEntity::new, ModBlocks.INFUSED_ETERIUM_FURNACE).build(null));

        IEWORKBENCH = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MysticalUniverse.MODID, "ieworkbench_crafting"),
                FabricBlockEntityTypeBuilder.create(IEWorkbenchBlockEntity::new, ModBlocks.INFUSED_ETERIUM_WORKBENCH).build(null));

        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, IEFURNACE);
    }
}
