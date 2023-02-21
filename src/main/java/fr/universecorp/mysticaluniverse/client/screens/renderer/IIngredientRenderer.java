package fr.universecorp.mysticaluniverse.client.screens.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;


// CREDIT: https://github.com/mezz/JustEnoughItems by mezz
// Under MIT-License: https://github.com/mezz/JustEnoughItems/blob/1.18/LICENSE.txt
public interface IIngredientRenderer<T> {

    default void render(MatrixStack stack, T ingredient) {
        render(stack, 0, 0, ingredient);
    }


    List<Text> getTooltip(T ingredient, TooltipContext tooltipFlag);


    default TextRenderer getFontRenderer(MinecraftClient minecraft, T ingredient) {
        return minecraft.textRenderer;
    }


    default int getWidth() {
        return 16;
    }


    default int getHeight() {
        return 16;
    }

    default void render(MatrixStack stack, int xPosition, int yPosition, @Nullable T ingredient) {

    }
}