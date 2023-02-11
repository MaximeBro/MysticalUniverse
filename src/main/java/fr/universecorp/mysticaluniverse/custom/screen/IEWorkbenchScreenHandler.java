package fr.universecorp.mysticaluniverse.custom.screen;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEWorkbenchBlockEntity;
import fr.universecorp.mysticaluniverse.custom.screen.slot.EssenceSlot;
import fr.universecorp.mysticaluniverse.custom.screen.slot.IECraftingResultSlot;
import fr.universecorp.mysticaluniverse.registry.ModScreenHandlers;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class IEWorkbenchScreenHandler extends ScreenHandler {
    public final IEWorkbenchBlockEntity blockEntity;
    private final PropertyDelegate propertyDelegate;
    public final Inventory inventory;
    public FluidStack fluidStack;

    public IEWorkbenchScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(3));
    }

    public IEWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity entity, PropertyDelegate delegate) {
        super(ModScreenHandlers.IEWORKBENCH_SCREEN_HANDLER, syncId);
        checkSize((Inventory) entity, 27);
        this.inventory = (Inventory) entity;
        this.inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;
        this.blockEntity = (IEWorkbenchBlockEntity) entity;
        this.fluidStack = new FluidStack(blockEntity.fluidStorage.variant, blockEntity.fluidStorage.amount);

        this.addSlot(new IECraftingResultSlot(this, playerInventory.player, this.inventory, 0,202 - 31, 49 - 29));  // Craft output Slot
        this.addSlot(new EssenceSlot(this.inventory, 1, 44 - 31, 52 - 32));                                                // Essence Slot

        int nbOfSlots = 0;
        int offSetY = -18;
        int offSetX = 0;
        for(int i=2; i < 27; i++) {
            if(nbOfSlots % 5 == 0) { offSetY+= 18; offSetX=0; }
            this.addSlot(new Slot(this.inventory, i, 75 - 31 + 18*offSetX, 13 - 29 + offSetY));
            nbOfSlots++;
            offSetX++;
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(delegate);
    }


    public void setFluid(FluidStack stack) {
        this.fluidStack = stack;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }


    public int getBubbleHeight() {
        int bubbleProgress = this.propertyDelegate.get(0);
        int maxBubble = this.propertyDelegate.get(1);
        int bubbleTextureHeight = 33;

        return maxBubble != 0 && bubbleProgress != 0 ? bubbleProgress * bubbleTextureHeight / maxBubble : 0;
    }


    public int isCraftAvailable() {
        return this.propertyDelegate.get(2);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 87 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 145));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
