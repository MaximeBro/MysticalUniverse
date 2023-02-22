package fr.universecorp.mysticaluniverse.custom.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class IEFurnaceRecipes implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;



    public IEFurnaceRecipes(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient) { return false; }

        return recipeItems.get(0).test(inventory.getStack(1));
    }


    // REI Integration needs (method only)
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);

        int i;
        for (i = 0; i < recipeItems.size(); i++) {
            ingredients.set(i, Ingredient.ofStacks(recipeItems.get(i).getMatchingStacks()));
        }
        ingredients.set(2, Ingredient.ofStacks(ModItems.ETERIUM_COAL.getDefaultStack()));

        return ingredients;
    }


    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.INFUSED_ETERIUM_FURNACE);
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<IEFurnaceRecipes> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "iefurnace_smelting";
    }

    public static class Serializer implements RecipeSerializer<IEFurnaceRecipes> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "iefurnace_smelting";

        @Override
        public IEFurnaceRecipes read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new IEFurnaceRecipes(id, output, inputs);
        }

        @Override
        public IEFurnaceRecipes read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new IEFurnaceRecipes(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, IEFurnaceRecipes recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
