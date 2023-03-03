package fr.universecorp.mysticaluniverse.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.client.renderers.FluidStackRenderer;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import fr.universecorp.mysticaluniverse.util.MouseUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IEWorkbenchScreen extends HandledScreen<IEWorkbenchScreenHandler> implements ScreenHandlerFactory {

    private static final Identifier TEXTURE =
            new Identifier(MysticalUniverse.MODID, "textures/gui/ieworkbench_gui.png");
    private PlayerInventory inventory;
    private FluidStackRenderer fluidStackRenderer;

    public IEWorkbenchScreen(IEWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title );
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        super.init();
        super.titleX = (backgroundWidth - textRenderer.getWidth(super.title)) / 2;
        super.titleY = -25;

        this.playerInventoryTitleY = backgroundHeight / 2 - 6;
        assignFluidStackRenderer();
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x - 31, y - 29, 0, 0, 239, 198);

        // Bubbling animation when consuming essence
        if(!this.handler.inventory.getStack(1).isEmpty() && this.handler.fluidStack.amount < this.handler.blockEntity.fluidStorage.getCapacity()) {
            drawTexture(matrices, x + 47 - 31, y + 20 - 32 + this.handler.getBubbleHeight(), 123, 201 + this.handler.getBubbleHeight(), 12, 34 - this.handler.getBubbleHeight());
        }

        // Crafting Arrow → Available
        if(this.handler.isCraftAvailable() > 0) {
            drawTexture(matrices, x + 169 - 31, y + 52 - 30, 99, 198, 22, 12);
        }

        // RecipeBook
        if(isMouseAboveArea(mouseX, mouseY, x + 173 - 31, y + 85 - 30, 0, 0, 19, 17)) {
            drawTexture(matrices, x + 173 - 31, y + 85 - 30, 136, 198, 20, 18);
        }

        // Fluid rendering in the storage
        fluidStackRenderer.drawFluid(matrices, handler.fluidStack, x - 15, y - 11, 8, 79,
                FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 20);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        this.textRenderer.draw(matrices, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.textRenderer.draw(matrices, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);

        renderFluidTooltip(matrices, mouseX, mouseY, x - 31 - 19 , y - 29, handler.fluidStack, 35, 17, fluidStackRenderer);

        // renderBook (next version)
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer(FluidStack.convertDropletsToMb(FluidConstants.BUCKET * 20),
                true, 8, 79);
    }

    private void renderFluidTooltip(MatrixStack matrices, int pMouseX, int pMouseY, int x, int y,
                                    FluidStack fluidStack, int offsetX, int offsetY, FluidStackRenderer renderer) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            renderTooltip(matrices, renderer.getTooltip(fluidStack, TooltipContext.Default.NORMAL),
                    Optional.empty(), pMouseX - x - 50, pMouseY - y - 25);
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

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double)left || mouseY < (double)top-18 || mouseX >= (double)(left + this.backgroundWidth + 21) || mouseY >= (double)(top + this.backgroundHeight);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        if(mouseX >= x + 173 - 31 && mouseX <= x + 192 - 30 &&
           mouseY >= y + 84 - 29  && mouseY <= y + 101 - 28) {

            PlayerEntity player = this.inventory.player;
            player.world.playSound(player, player.getBlockPos(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.2f, 1f);

            IEWbRecipeBookScreenHandler screenHandler = this.createMenu(player.getId(), this.inventory, player);
            callRecipeScreen(screenHandler, player, this); // → Opens the ieworkbench recipe book showing
            return true;                                                   // every recipe handled by the ieworkbench ...
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public static void callRecipeScreen(IEWbRecipeBookScreenHandler screenHandler, PlayerEntity player, IEWorkbenchScreen screen) {
        IEWbRecipeBookScreen wbScreen = new IEWbRecipeBookScreen(screenHandler, player.getInventory(), Text.of("Mystical RecipeBook"));
        wbScreen.setParentScreen(screen);
        MinecraftClient.getInstance().setScreen(wbScreen);
    }

    @Nullable
    @Override
    public IEWbRecipeBookScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        IEWbRecipeBookScreenHandler screenHandler = new IEWbRecipeBookScreenHandler(syncId, inv);

        FluidStack fluidStack = new FluidStack(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER), 500);
        screenHandler.setFluidStack(fluidStack);

        return screenHandler;
    }
}
