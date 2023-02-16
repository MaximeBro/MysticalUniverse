package fr.universecorp.mysticaluniverse.custom.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.screen.renderer.FluidStackRenderer;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import fr.universecorp.mysticaluniverse.util.MouseUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class IEWbRecipeBookScreen extends HandledScreen<IEWbRecipeBookScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(MysticalUniverse.MODID, "textures/gui/ieworkbench_recipebook_gui.png");

    private FluidStackRenderer fluidStackRenderer;
    private PlayerInventory inventory;

    private int recipeX;
    private int recipeY;


    public IEWbRecipeBookScreen(IEWbRecipeBookScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 4 - 12;
        this.recipeX = (backgroundWidth - textRenderer.getWidth(this.handler.recipeTitle)) / 2 - 10;
        this.recipeY = backgroundHeight / 2 + 27;
        assignFluidStackRenderer();
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, 197, 134);

        // Previous Page
        if(isMouseAboveArea(mouseX, mouseY, x + 142, y + 117, 0, 0, 18, 10)) {
            drawTexture(matrices, x + 142, y + 117, 198, 1, 18, 10);
        }

        // Next Page
        if(isMouseAboveArea(mouseX, mouseY, x + 165, y + 117, 0, 0, 18, 10)) {
            drawTexture(matrices, x + 165, y + 117, 221, 1, 18, 10);
        }

        // Close Button
        if(isMouseAboveArea(mouseX, mouseY, x + 182, y + 5, 0, 0, 8, 7)) {
            drawTexture(matrices, x + 182, y + 5, 198, 13, 9, 8);
        }

        fluidStackRenderer.drawFluid(matrices, handler.fluidStack, x + 13, y + 23, 8, 79,
                2500);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        this.textRenderer.draw(matrices, this.title, (float)this.titleX, (float)this.titleY, 0x404040);
        this.textRenderer.draw(matrices, this.handler.recipeTitle, (float)this.recipeX, (float)this.recipeY, 0x404040);

        renderFluidTooltip(matrices, mouseX, mouseY, x + 12, y + 22, handler.fluidStack, 1, 1, fluidStackRenderer);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // Previous Page
        if(mouseX >= x + 142 && mouseX <= x + 159 &&
           mouseY >= y + 117 && mouseY <= y + 126) {

            PlayerEntity player = this.inventory.player;
            player.world.playSound(player, player.getBlockPos(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1f, 1f);
            this.handler.changeRecipe();
            return true;
        }

        // Next Page
        if(mouseX >= x + 165 && mouseX <= x + 182 &&
           mouseY >= y + 117 && mouseY <= y + 126) {

            PlayerEntity player = this.inventory.player;
            player.world.playSound(player, player.getBlockPos(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1f, 1f);
            this.handler.changeRecipe();
            return true;
        }

        // Close Button
        if(mouseX >= x + 182 && mouseX <= x + 191 &&
           mouseY >= y + 5   && mouseY <= y + 13) {
            PlayerEntity player = this.inventory.player;
            player.world.playSound(player, player.getBlockPos(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5f, 1f);
            player.closeHandledScreen();
            return true;
        }


        return false;
    }


    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidStackRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private void renderFluidTooltip(MatrixStack matrices, int pMouseX, int pMouseY, int x, int y,
                                    FluidStack fluidStack, int offsetX, int offsetY, FluidStackRenderer renderer) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            renderTooltip(matrices, renderer.getTooltip(fluidStack, TooltipContext.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer(2500,
                false, 8, 79);
    }
}
