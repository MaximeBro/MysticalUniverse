package fr.universecorp.mysticaluniverse.custom.screen.slot;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEWorkbenchBlockEntity;
import fr.universecorp.mysticaluniverse.custom.screen.IEWorkbenchScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import static fr.universecorp.mysticaluniverse.custom.blocks.entity.IEWorkbenchBlockEntity.extractFluid;

public class IECraftingResultSlot extends Slot {
    private final PlayerEntity player;
    private final IEWorkbenchScreenHandler handler;

    private int amount;
    public IECraftingResultSlot(IEWorkbenchScreenHandler handler, PlayerEntity player, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.handler = handler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return stack.getMaxCount();
    }

    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }

        return super.takeStack(amount);
    }

    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        super.onTakeItem(player, stack);

        for(int i=2; i < 27; i++) {
            this.inventory.setStack(i, new ItemStack(this.inventory.getStack(i).getItem(), this.inventory.getStack(i).getCount()-1));
        }

        IEWorkbenchBlockEntity entity = this.handler.blockEntity;
        extractFluid(entity);

        this.markDirty();
        this.inventory.markDirty();
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    protected void onCrafted(ItemStack stack) {
        stack.onCraft(this.player.world, this.player, this.amount);
    }
}
