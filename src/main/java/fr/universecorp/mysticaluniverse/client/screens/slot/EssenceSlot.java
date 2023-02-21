package fr.universecorp.mysticaluniverse.client.screens.slot;

import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class EssenceSlot extends Slot {
    public EssenceSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem().equals(ModItems.BLUE_CLEMATITE_ESSENCE);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return stack.getMaxCount();
    }
}
