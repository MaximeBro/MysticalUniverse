package fr.universecorp.mysticaluniverse.custom.blocks.renderer;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class IEComposterFluidRenderer implements BlockEntityRenderer<IEComposterEntity> {

    public IEComposterFluidRenderer(BlockEntityRendererFactory.Context ctx) { }

    @Override
    public void render(IEComposterEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {


        int amount = (int) entity.fluidStorage.amount;
        int level = 0;

        if(amount == 0) { level = 0; }

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

        Fluid fluid = ModFluids.STILL_LIQUID_ETHER;
        FluidRenderHandler fluidHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
        BlockState fluidBlock = fluid.getDefaultState().getBlockState();

        int fluidColor = fluidHandler.getFluidColor(null, null, fluid.getDefaultState());
        int fluidLight = fluidBlock.getLuminance() > 0 ? 15728880 : light;

        float height = level == 0 ? 0 : 0.2f*level + 0.3f;

        int fluidR = (fluidColor >> 16) & 0xFF;
        int fluidG = (fluidColor >> 8) & 0xFF;
        int fluidB = (fluidColor) & 0xFF;

        Sprite fluidSprite = fluidHandler.getFluidSprites(null, null, fluid.getDefaultState())[0];

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(fluidSprite.getId().getNamespace(), "textures/" + fluidSprite.getId().getPath() + ".png")));
        MatrixStack.Entry matrixEntry = matrices.peek();
        Matrix4f model = matrixEntry.getPositionMatrix();
        Matrix3f normal = matrixEntry.getNormalMatrix();

        consumer.vertex(model,0, height, 0).color(fluidR, fluidG, fluidB, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,1, height, 0).color(fluidR, fluidG, fluidB, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,1, height, 1).color(fluidR, fluidG, fluidB, 255).texture(1.F, 1.F / fluidSprite.getHeight()).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        consumer.vertex(model,0, height, 1).color(fluidR, fluidG, fluidB, 255).texture(0.F, 1.F / fluidSprite.getHeight()).overlay(OverlayTexture.DEFAULT_UV).light(fluidLight).normal(normal, 0, 1, 0).next();
        matrices.pop();

        //System.out.println(entity.getPos() + " mb : " + amount);
    }
}
