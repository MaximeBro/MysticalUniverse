package fr.universecorp.mysticaluniverse.client.rei;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.recipe.IEComposterRecipes;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IEComposterREIDisplay extends BasicDisplay {

    public static final CategoryIdentifier<IEComposterREIDisplay> ID =
            CategoryIdentifier.of(MysticalUniverse.MODID, "iecomposter");

    public IEComposterREIDisplay(IEComposterRecipes recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()),
                List.of(EntryIngredients.of(recipe.getOutput()), EntryIngredients.of(ModFluids.STILL_LIQUID_ETHER)),
                Optional.ofNullable(recipe.getId()));
    }

    public IEComposterREIDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return this.ID;
    }
}
