package fr.universecorp.mysticaluniverse.custom.recipe;

import fr.universecorp.mysticaluniverse.custom.screen.IEWorkbenchScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;

import java.util.Iterator;

public class IEWorkbenchCraftingInventory implements Inventory, RecipeInputProvider {
    private final DefaultedList<ItemStack> stacks;
    private final int width;
    private final int height;
    private final IEWorkbenchScreenHandler handler;

    public IEWorkbenchCraftingInventory(IEWorkbenchScreenHandler handler, int width, int height) {
        this.stacks = DefaultedList.ofSize(width * height, ItemStack.EMPTY);
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public int size() {
        return this.stacks.size();
    }

    public boolean isEmpty() {
        Iterator iter = this.stacks.iterator();

        ItemStack itemStack;
        do {
            if (!iter.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)iter.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return slot >= this.size() ? ItemStack.EMPTY : (ItemStack)this.stacks.get(slot);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.stacks, slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.stacks, slot, amount);
        if (!itemStack.isEmpty()) {
            this.handler.onContentChanged(this);
        }

        return itemStack;
    }

    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
        this.handler.onContentChanged(this);
    }

    public void markDirty() {
    }

    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public void clear() {
        this.stacks.clear();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void provideRecipeInputs(RecipeMatcher finder) {
        Iterator iter = this.stacks.iterator();

        while(iter.hasNext()) {
            ItemStack itemStack = (ItemStack)iter.next();
            finder.addUnenchantedInput(itemStack);
        }

    }
}

