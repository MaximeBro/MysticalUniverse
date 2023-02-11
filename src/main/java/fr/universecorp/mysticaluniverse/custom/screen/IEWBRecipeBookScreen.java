package fr.universecorp.mysticaluniverse.custom.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticaluniverse.MysticalUniverse;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IEWBRecipeBookScreen extends HandledScreen<IEWbRecipeBookScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(MysticalUniverse.MODID, "textures/gui/ieworkbench_recipebook_gui.png");


    public IEWBRecipeBookScreen(IEWbRecipeBookScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x - 31, y - 29, 0, 0, 186, 134);
    }
}
