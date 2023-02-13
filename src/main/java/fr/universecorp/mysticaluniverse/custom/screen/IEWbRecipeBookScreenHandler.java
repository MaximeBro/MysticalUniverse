package fr.universecorp.mysticaluniverse.custom.screen;

import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import fr.universecorp.mysticaluniverse.registry.ModScreenHandlers;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class IEWbRecipeBookScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    public FluidStack fluidStack;

    public ArrayList<ItemStack[]> alRecipes;
    public Text recipeTitle;


    public IEWbRecipeBookScreenHandler(int syncId, PlayerInventory inventory) {
        super(ModScreenHandlers.IEWB_RECIPEBOOK_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(26);
        this.inventory.onOpen(inventory.player);

        int nbOfSlots = 0;
        int offSetY = -18;
        int offSetX = 0;
        for(int i=0; i < 25; i++) {
            if(nbOfSlots % 5 == 0) { offSetY+= 18; offSetX=0; }
            this.addSlot(new Slot(this.inventory, i, 33 + 18*offSetX, 19 + offSetY));
            nbOfSlots++;
            offSetX++;
        }

        this.addSlot(new Slot(this.inventory, 25, 160, 55));

        this.alRecipes = new ArrayList<ItemStack[]>();
        this.initializeRecipes(this.alRecipes);
        this.initializeFirstRecipe(this.alRecipes);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }

    public void initializeFirstRecipe(ArrayList<ItemStack[]> alRecipesList) {

        ItemStack[] tabStacks = alRecipesList.get(0);
        for(int i=0; i < this.slots.size(); i++) {
            this.slots.get(i).setStack(tabStacks[i]);
        }

        this.recipeTitle = Text.of("Infused Armor Core");
    }

    public void initializeRecipes(ArrayList<ItemStack[]> alRecipesList) {
        ItemStack[] tabStacks = new ItemStack[26];

        // Hard-coded recipes
        // ------------------


        // First Recipe -> Infused Armor Core
        // First Line
        tabStacks[0] = new ItemStack(ModBlocks.ETERIUM_BLOCK);
        tabStacks[1] = new ItemStack(Items.IRON_INGOT);
        tabStacks[2] = new ItemStack(Items.IRON_INGOT);
        tabStacks[3] = new ItemStack(Items.IRON_INGOT);
        tabStacks[4] = new ItemStack(ModBlocks.ETERIUM_BLOCK);

        // Second Line
        tabStacks[5] = new ItemStack(Items.IRON_INGOT);
        tabStacks[6] = new ItemStack(ModItems.CHARGED_ETERIUM_INGOT);
        tabStacks[7] = new ItemStack(ModBlocks.ETERIUM_BLOCK);
        tabStacks[8] = new ItemStack(ModItems.CHARGED_ETERIUM_INGOT);
        tabStacks[9] = new ItemStack(Items.IRON_INGOT);

        // Third Line
        tabStacks[10] = new ItemStack(Items.IRON_INGOT);
        tabStacks[11] = new ItemStack(ModBlocks.ETERIUM_BLOCK);
        tabStacks[12] = new ItemStack(ModItems.ETERIUM_ARMOR_CORE);
        tabStacks[13] = new ItemStack(ModBlocks.ETERIUM_BLOCK);
        tabStacks[14] = new ItemStack(Items.IRON_INGOT);

        // Fourth Line
        tabStacks[15] = new ItemStack(Items.IRON_INGOT);
        tabStacks[16] = new ItemStack(ModItems.CHARGED_ETERIUM_INGOT);
        tabStacks[17] = new ItemStack(ModBlocks.ETERIUM_BLOCK);
        tabStacks[18] = new ItemStack(ModItems.CHARGED_ETERIUM_INGOT);
        tabStacks[19] = new ItemStack(Items.IRON_INGOT);

        // Fifth Line
        tabStacks[20] = new ItemStack(ModBlocks.ETERIUM_BLOCK);
        tabStacks[21] = new ItemStack(Items.IRON_INGOT);
        tabStacks[22] = new ItemStack(Items.IRON_INGOT);
        tabStacks[23] = new ItemStack(Items.IRON_INGOT);
        tabStacks[24] = new ItemStack(ModBlocks.ETERIUM_BLOCK);

        // Output
        tabStacks[25] = new ItemStack(ModItems.INFUSED_ARMOR_CORE);

        alRecipesList.add(tabStacks);
    }
}
