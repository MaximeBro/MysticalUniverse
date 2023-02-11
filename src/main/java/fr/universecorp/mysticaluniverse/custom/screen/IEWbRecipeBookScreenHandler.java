package fr.universecorp.mysticaluniverse.custom.screen;

import fr.universecorp.mysticaluniverse.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class IEWbRecipeBookScreenHandler extends ScreenHandler {

    private final Inventory inventory;


    public IEWbRecipeBookScreenHandler(int syncId, PlayerInventory inventory) {
        super(ModScreenHandlers.IEWB_RECIPEBOOK_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(26);
        this.inventory.onOpen(inventory.player);

        int nbOfSlots = 0;
        int offSetY = -18;
        int offSetX = 0;
        for(int i=0; i < 25; i++) {
            if(nbOfSlots % 5 == 0) { offSetY+= 18; offSetX=0; }
            this.addSlot(new Slot(this.inventory, i, 22 + 18*offSetX, 19 + offSetY));
            nbOfSlots++;
            offSetX++;
        }

        this.addSlot(new Slot(this.inventory, 25, 149, 55));
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}
