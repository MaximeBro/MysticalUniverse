package fr.universecorp.mysticaluniverse.client.rei;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticaluniverse.client.screens.IEFurnaceScreen;
import fr.universecorp.mysticaluniverse.client.screens.IEWorkbenchScreen;
import fr.universecorp.mysticaluniverse.custom.recipe.IEComposterRecipes;
import fr.universecorp.mysticaluniverse.custom.recipe.IEFurnaceRecipes;
import fr.universecorp.mysticaluniverse.custom.recipe.IEWorkbenchShapedRecipes;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Arrow;
import me.shedaniel.rei.api.client.gui.widgets.BurningFire;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.util.List;


/*
 * HUGE THANKS TO Tarentel FOR HIS HELP ABOUT BASIC REI INTEGRATION !
 * https://linktr.ee/tarantel
 */

@Environment(EnvType.CLIENT)
public class REIPlugin implements REIClientPlugin {

    public REIPlugin() { }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(List.of(
                new IEFurnaceREICategory(),
                new IEWorkbenchREICategory(),
                new IEComposterCategory()
        ));

        registry.addWorkstations(
            IEFurnaceREIDisplay.ID, EntryStacks.of(ModBlocks.INFUSED_ETERIUM_FURNACE)
        );

        registry.addWorkstations(
            IEWorkbenchREIDisplay.ID, EntryStacks.of(ModBlocks.INFUSED_ETERIUM_WORKBENCH)
        );

        registry.addWorkstations(
            IEComposterREIDisplay.ID, EntryStacks.of(ModBlocks.INFUSED_ETERIUM_COMPOSTER)
        );

    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(IEFurnaceRecipes.class, IEFurnaceREIDisplay::new);
        registry.registerFiller(IEWorkbenchShapedRecipes.class, IEWorkbenchREIDisplay::new);
        registry.registerFiller(IEComposterRecipes.class, IEComposterREIDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(83, 37, 25, 10), IEFurnaceScreen.class, IEFurnaceREIDisplay.ID);
        registry.registerContainerClickArea(new Rectangle(135, 23, 25, 10), IEWorkbenchScreen.class, IEWorkbenchREIDisplay.ID);
    }


    // Utils for Categories
    public static Slot createInputSlot(BasicDisplay display, int index, int x, int y) {
        if (index >= display.getInputEntries().size()) {
            return Widgets.createSlot(new Point(x, y));
        }
        EntryIngredient ingredient = display.getInputEntries().get(index);
        return Widgets.createSlot(new Point(x, y)).entries(ingredient).markInput();
    }

    public static Slot createOutputSlot(BasicDisplay display, int index, int x, int y) {
        if (index >= display.getOutputEntries().size()) {
            return Widgets.createSlot(new Point(x, y));
        }
        EntryIngredient outputIngredient = display.getOutputEntries().get(index);
        return Widgets.createSlot(new Point(x, y)).entries(outputIngredient).disableBackground().markOutput();
    }

    public static Arrow createAnimatedArrow(int x, int y) {
        return Widgets.createArrow(new Point(x, y)).animationDurationTicks(60);
    }

    public static BurningFire createBurningFire(int x, int y) {
        return Widgets.createBurningFire(new Point(x, y)).animationDurationTicks(20*20);
    }

    public class DrawUtil {
        public static void drawFluid(DrawableHelper helper, MatrixStack matrices, Identifier id, int u, int v, int x, int y, int width, int height, int amount) {
            // Drawing storage texture
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, id);
            helper.drawTexture(matrices, x, y, u, v, width, height);


            // Drawing fluid sprites
            RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

            FluidVariant variant = FluidVariant.of(ModFluids.STILL_LIQUID_ETHER);
            y += height;
            final Sprite sprite = FluidVariantRendering.getSprite(variant);
            int color = FluidVariantRendering.getColor(variant);

            final int iconHeight = sprite.getHeight();
            int offsetHeight = amount;

            RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, 1F);

            int iteration = 0;
            while (offsetHeight != 0) {
                final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;

                helper.drawSprite(matrices, x + 4, y - offsetHeight - 4, 0, width - 8, curHeight, sprite);
                offsetHeight -= curHeight;
                iteration++;
                if (iteration > 50) {
                    break;
                }
            }
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/block/water_still.png"));
        }

        public static void drawFluid(DrawableHelper helper, MatrixStack matrices, int x, int y, int width, int height) {

            // Drawing fluid sprites
            RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

            FluidVariant variant = FluidVariant.of(ModFluids.STILL_LIQUID_ETHER);
            y += height;
            final Sprite sprite = FluidVariantRendering.getSprite(variant);
            int color = FluidVariantRendering.getColor(variant);

            final int iconHeight = sprite.getHeight();
            int offsetHeight = height;

            RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, 1F);

            int iteration = 0;
            while (offsetHeight != 0) {
                final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;

                helper.drawSprite(matrices, x , y - offsetHeight, 0, width, curHeight, sprite);
                offsetHeight -= curHeight;
                iteration++;
                if (iteration > 50) {
                    break;
                }
            }
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/block/water_still.png"));
        }
    }
}
