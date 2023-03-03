package fr.universecorp.mysticaluniverse.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.client.renderers.FluidStackRenderer;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import fr.universecorp.mysticaluniverse.util.MouseUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class IEFurnaceScreen extends HandledScreen<IEFurnaceScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(MysticalUniverse.MODID, "textures/gui/iefurnace_gui.png");

    private FluidStackRenderer fluidStackRenderer;

    public IEFurnaceScreen(IEFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        this.playerInventoryTitleY+= 3;
        assignFluidStackRenderer();
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer(FluidStack.convertDropletsToMb(FluidConstants.BUCKET * 10),
                true, 8, 52);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        this.textRenderer.draw(matrices, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.textRenderer.draw(matrices, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);

        renderFluidTooltip(matrices, mouseX, mouseY, x, y, handler.fluidStack, 35, 17, fluidStackRenderer);
    }

    private void renderFluidTooltip(MatrixStack matrices, int pMouseX, int pMouseY, int x, int y,
                                    FluidStack fluidStack, int offsetX, int offsetY, FluidStackRenderer renderer) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            renderTooltip(matrices, renderer.getTooltip(fluidStack, TooltipContext.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(matrices, x, y);
        //drawTexture(matrices, x + 35, y + 69 - handler.getScaledFluidProgress(), 177, 78 - handler.getScaledFluidProgress(), 8, handler.getScaledFluidProgress());
        fluidStackRenderer.drawFluid(matrices, handler.fluidStack, x + 35, y + 17, 8, 52,
              FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10);
    }

    private void renderProgressArrow(MatrixStack matrices, int x, int y) {
        if(handler.isCrafting()) {
            drawTexture(matrices, x + 85, y + 37, 177, 15, handler.getScaledProgress(), 10);
        }

        if(handler.hasFuel()) {
            drawTexture(matrices, x + 62, y +37 + 13 - handler.getScaledFuelProgress(), 176,
                    13 - handler.getScaledFuelProgress(), 13, handler.getScaledFuelProgress());
        }
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidStackRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
}
