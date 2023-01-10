package fr.universecorp.mysticaluniverse.custom.screen.slot;

import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ModResultSlot extends Slot {
    public ModResultSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return stack.getMaxCount();
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        if(stack.getItem() == ModItems.ETERIUM_INGOT) {
            player.addExperience((int) (stack.getCount()*0.7));
        }
    }
}
