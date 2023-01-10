package fr.universecorp.mysticaluniverse.custom.recipe;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {

    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MysticalUniverse.MODID, EteriumIngotRecipe.Serializer.ID),
                EteriumIngotRecipe.Serializer.INSTANCE
        );

        Registry.register(Registry.RECIPE_TYPE, new Identifier(MysticalUniverse.MODID, EteriumIngotRecipe.Type.ID),
                EteriumIngotRecipe.Type.INSTANCE
        );
    }
}
