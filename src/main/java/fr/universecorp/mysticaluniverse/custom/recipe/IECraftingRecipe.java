package fr.universecorp.mysticaluniverse.custom.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

public interface IECraftingRecipe extends Recipe<IEWorkbenchCraftingInventory> {
    default RecipeType<?> getType() { return IERecipeType.CRAFTING; }
}
