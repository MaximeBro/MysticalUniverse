package fr.universecorp.mysticaluniverse.client.rei;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.recipe.IEFurnaceRecipes;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IEFurnaceREIDisplay extends BasicDisplay {

    public static final CategoryIdentifier<IEFurnaceREIDisplay> ID =
            CategoryIdentifier.of(MysticalUniverse.MODID, "iefurnace");

    public IEFurnaceREIDisplay(IEFurnaceRecipes recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), 
             Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
             Optional.ofNullable(recipe.getId()));
    }

    public IEFurnaceREIDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }
}
