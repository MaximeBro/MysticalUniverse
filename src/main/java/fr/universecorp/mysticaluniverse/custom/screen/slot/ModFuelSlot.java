package fr.universecorp.mysticaluniverse.custom.screen.slot;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ModFuelSlot extends Slot {
    public ModFuelSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return IEFurnaceBlockEntity.canUseAsFuel(stack);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return stack.getMaxCount();
    }
}