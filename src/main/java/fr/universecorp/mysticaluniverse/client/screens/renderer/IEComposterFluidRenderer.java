package fr.universecorp.mysticaluniverse.client.screens.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class IEComposterFluidRenderer implements BlockEntityRenderer<IEComposterEntity> {

    private int renderTime = 0;

    public IEComposterFluidRenderer(BlockEntityRendererFactory.Context ctx) { }

    @Override
    public void render(IEComposterEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if(renderTime >= 360) { renderTime = 0; }
        else                  { renderTime+= 1; }

        Fluid fluid = entity.fluidStorage.variant.getFluid();
        FluidRenderHandler fluidHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);

        if(fluidHandler == null) { return; }

        int amount = (int) entity.fluidStorage.amount;
        int level = 0;
        if(amount == 0) { level = 0; }

        matrices.push();
        if(amount > 0) {

            ItemStack stack = amount == 1000 ? ModFluids.LIQUID_ETHER_BUCKET.getDefaultStack() : ModItems.BLUE_CLEMATITE_ESSENCE.getDefaultStack();

            double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
            matrices.translate(0.5, 1.15 + offset/2, 0.5);

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion( renderTime ));

            int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
        }
        matrices.pop();

        matrices.push();

        if(amount >= 100 && amount < 500) {
            level = 1;
        }

        if(amount >= 500 && amount < 1000) {
            level = 2;
        }

        if(amount == 1000) {
            level = 3;
        }

        BlockState fluidBlock = fluid.getDefaultState().getBlockState();

        int fluidColor = fluidHandler.getFluidColor(null, null, fluid.getDefaultState());
        int fluidLight = fluidBlock.getLuminance() > 0 ? 15728880 : light;

        float height = level == 0 ? 0 : 0.2f*level + 0.3f;

        int fluidR = (fluidColor >> 16) & 0xFF;
        int fluidG = (fluidColor >> 8) & 0xFF;
        int fluidB = (fluidColor) & 0xFF;

        Sprite fluidSprite = fluidHandler.getFluidSprites(null, null, fluid.getDefaultState())[0];
        Sprite fluidSprite2 = fluidHandler.getFluidSprites(null, null, fluid.getDefaultState())[1];

        Identifier id = new Identifier(fluidSprite.getId().getNamespace(), "textures/" + fluidSprite.getId().getPath() + ".png");

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(id, false));
        MatrixStack.Entry matrixEntry = matrices.peek();
        Matrix4f model = matrixEntry.getPositionMatrix();
        Matrix3f normal = matrixEntry.getNormalMatrix();

        consumer.vertex(model,0, height, 0).color(fluidR, fluidG, fluidB, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,1, height, 0).color(fluidR, fluidG, fluidB, 255).texture(1.f, 0).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,1, height, 1).color(fluidR, fluidG, fluidB, 255).texture(1.f, 1.f / fluidSprite2.getHeight()).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,0, height, 1).color(fluidR, fluidG, fluidB, 255).texture(0.f, 1.f / fluidSprite2.getHeight()).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();

        matrices.pop();
    }
}
