package fr.universecorp.mysticaluniverse.custom.screen.slot;

import fr.universecorp.mysticaluniverse.registry.ModFluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class FurnaceIngredientSlot extends Slot {
    public FurnaceIngredientSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return !stack.equals(ModFluids.LIQUID_ETHER_BUCKET) && !stack.equals(Items.BUCKET);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return super.getMaxItemCount(stack);
    }
}
