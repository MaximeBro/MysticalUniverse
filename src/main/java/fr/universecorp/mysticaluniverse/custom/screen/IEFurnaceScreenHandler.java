package fr.universecorp.mysticaluniverse.custom.screen;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEFurnaceBlockEntity;
import fr.universecorp.mysticaluniverse.custom.screen.slot.ModFuelSlot;
import fr.universecorp.mysticaluniverse.custom.screen.slot.ModResultSlot;
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

public class IEFurnaceScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final IEFurnaceBlockEntity blockEntity;
    public FluidStack fluidStack;

    public IEFurnaceScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(4));
    }

    public IEFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity entity, PropertyDelegate delegate) {
        super(ModScreenHandlers.IEFURNACE_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) entity), 4);
        this.inventory = (Inventory) entity;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;
        this.blockEntity = (IEFurnaceBlockEntity) entity;
        this.fluidStack = new FluidStack(blockEntity.fluidStorage.variant, blockEntity.fluidStorage.amount);

        this.addSlot(new ModFuelSlot(inventory, 0, 61, 53));                            // Fuel Slot
        this.addSlot(new Slot(inventory, 1, 61, 17));                                   // Ingredient Slot
        this.addSlot(new ModResultSlot(playerInventory.player, inventory, 2, 121, 35)); // Recipe output Slot
        this.addSlot(new Slot(inventory, 3, 9, 17));                                   // Fluid Slot (Liquid Ether Bucket)
        this.addSlot(new Slot(inventory, 4, 9, 53));                                   // Fluid Slot (Empty Buckets)

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(delegate);
    }


    public void setFluid(FluidStack stack) {
        this.fluidStack = stack;
    }

    public boolean isCrafting() {
        return this.propertyDelegate.get(0) > 0;
    }

    public boolean hasFuel() { return this.propertyDelegate.get(2) > 0; }

    public int getScaledFluidProgress() {
        int fluidProgress = (int) this.fluidStack.amount;
        int maxCapacity = 10000;
        int fluidProgressSize = 52;

        return (int) ( (float)fluidProgress / (float)maxCapacity * fluidProgressSize );
    }

    public int getScaledFuelProgress() {
        int fuelProgress = this.propertyDelegate.get(2);
        int maxFuelProgress = this.propertyDelegate.get(3);
        int fuelProgressSize = 14;

        return maxFuelProgress != 0 ? (int) ( ((float)fuelProgress / (float)maxFuelProgress) * fuelProgressSize) : 0;
    }
    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
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


    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
