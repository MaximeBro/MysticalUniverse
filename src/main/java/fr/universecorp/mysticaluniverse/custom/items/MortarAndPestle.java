package fr.universecorp.mysticaluniverse.custom.items;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MortarAndPestle extends Item implements FabricItem {
    public MortarAndPestle(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return this.getDefaultStack();
    }
}
