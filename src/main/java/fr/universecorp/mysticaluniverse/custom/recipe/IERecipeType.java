package fr.universecorp.mysticaluniverse.custom.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface IERecipeType<T extends Recipe<?>> {

    RecipeType<IEWorkbenchShapedRecipes> CRAFTING = register("ieworkbench_crafting");

    static <T extends Recipe<?>> RecipeType<T> register(final String id) {
        return (RecipeType) Registry.register(Registry.RECIPE_TYPE, new Identifier(id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }
}
