package fr.universecorp.mysticaluniverse.client.rei;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.recipe.IEWorkbenchCraftingInventory;
import fr.universecorp.mysticaluniverse.custom.recipe.IEWorkbenchShapedRecipes;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IEWorkbenchRecipeREIDisplay<T extends Recipe<IEWorkbenchCraftingInventory>> extends BasicDisplay {

    public static final CategoryIdentifier<IEWorkbenchRecipeREIDisplay> ID =
            CategoryIdentifier.of(MysticalUniverse.MODID, "iecrafting");

    public IEWorkbenchRecipeREIDisplay(IEWorkbenchShapedRecipes recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()),
                Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                Optional.ofNullable(recipe.getId()));
    }

    public IEWorkbenchRecipeREIDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }
}
