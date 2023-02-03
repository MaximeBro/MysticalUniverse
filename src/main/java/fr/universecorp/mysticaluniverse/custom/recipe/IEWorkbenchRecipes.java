package fr.universecorp.mysticaluniverse.custom.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class IEWorkbenchRecipes implements Recipe<SimpleInventory> {

    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public IEWorkbenchRecipes(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient) { return false; }

        /*
        for(int i=0; i < this.recipeItems.size(); i++)
            if(!this.recipeItems.get(i).test(inventory.getStack(this.ingredientSlots[i])))
                return false;*/

        return true;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return this.output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output.copy();
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public DefaultedList<Ingredient> getRecipeItems() { return this.recipeItems; }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.INFUSED_ETERIUM_WORKBENCH);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<IEWorkbenchRecipes> {
        private Type() { }
        public static final IEWorkbenchRecipes.Type INSTANCE = new IEWorkbenchRecipes.Type();
        public static final String ID = "ieworkbench_crafting";
    }

    public static class Serializer implements RecipeSerializer<IEWorkbenchRecipes> {
        public static final IEWorkbenchRecipes.Serializer INSTANCE = new IEWorkbenchRecipes.Serializer();
        public static final String ID = "ieworkbench_crafting";

        @Override
        public IEWorkbenchRecipes read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new IEWorkbenchRecipes(id, output, inputs);
        }

        @Override
        public IEWorkbenchRecipes read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new IEWorkbenchRecipes(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, IEWorkbenchRecipes recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
