package fr.universecorp.mysticaluniverse.custom.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class IEComposterRecipes implements Recipe<SimpleInventory> {

    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public IEComposterRecipes(Identifier id,  ItemStack output, DefaultedList<Ingredient> inputs) {
        this.id = id;
        this.recipeItems = inputs;
        this.output = output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);

        int i;
        for (i = 0; i < recipeItems.size(); i++) {
            ingredients.set(i, Ingredient.ofStacks(recipeItems.get(i).getMatchingStacks()));
        }

        return ingredients;
    }

    @Override
    public ItemStack getOutput() {
        return ModFluids.LIQUID_ETHER_BLOCK.asItem().getDefaultStack().copy();
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
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

    public static class Type implements RecipeType<IEComposterRecipes> {
        private Type() { }
        public static final IEComposterRecipes.Type INSTANCE = new IEComposterRecipes.Type();
        public static final String ID = "iecomposter_composting";
    }

    public static class Serializer implements RecipeSerializer<IEComposterRecipes> {
        public static final IEComposterRecipes.Serializer INSTANCE = new IEComposterRecipes.Serializer();
        public static final String ID = "iecomposter_composting";

        @Override
        public IEComposterRecipes read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new IEComposterRecipes(id, output, inputs);
        }

        @Override
        public IEComposterRecipes read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new IEComposterRecipes(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, IEComposterRecipes recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
