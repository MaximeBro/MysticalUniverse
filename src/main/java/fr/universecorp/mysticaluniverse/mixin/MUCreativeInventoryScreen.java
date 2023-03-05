package fr.universecorp.mysticaluniverse.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CreativeInventoryScreen.class)
public abstract class MUCreativeInventoryScreen extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {

    private static final Identifier TEXTURE_TABS
            = new Identifier("textures/gui/container/creative_inventory/tabs_mystical_tab.png");

    private static final Identifier TEXTURE
            = new Identifier("textures/gui/container/creative_inventory/tabs.png");

    public MUCreativeInventoryScreen(PlayerEntity player) {
        super(new CreativeInventoryScreen.CreativeScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
        this.backgroundHeight = 136;
        this.backgroundWidth = 195;
    }

    @Shadow
    private static int selectedTab;

    @Inject(method = "renderTabIcon", at = @At(value = "RETURN"), cancellable = true)
    public void renderTabIcon(MatrixStack matrices, ItemGroup group, CallbackInfo info) {

        if(group.getIcon().getItem().equals(ModItems.ICON.asItem())) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE_TABS);

            boolean bl = group.getIndex() == selectedTab;
            boolean bl2 = group.isTopRow();
            int i = group.getColumn();
            int u = i * 28;
            int v = 0;
            int posX = this.x + 28 * i;
            int posY = this.y;
            int n = 32;
            if (bl) {
                v += 32;
            }
            if (group.isSpecial()) {
                posX = this.x + this.backgroundWidth - 28 * (6 - i);
            } else if (i > 0) {
                posX += i;
            }
            if (bl2) {
                posY -= 28;
            } else {
                v += 64;
                posY += this.backgroundHeight - 4;
            }
            this.drawTexture(matrices, posX, posY, u, v, 28, 32);
            this.itemRenderer.zOffset = 100.0f;
            int n2 = bl2 ? 1 : -1;
            ItemStack itemStack = group.getIcon();
            this.itemRenderer.renderInGuiWithOverrides(itemStack, posX += 6, posY += 8 + n2);
            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack, posX, posY);
            this.itemRenderer.zOffset = 0.0f;
        } else {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
        }
    }
}
