package fr.universecorp.mysticaluniverse.client.screens.renderer;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
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

        if(renderTime >= 3600) { renderTime = 0; }
        else                  { renderTime+= 1; }

        Fluid fluid = entity.fluidStorage.variant.getFluid();
        FluidRenderHandler fluidHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);

        if(fluidHandler == null) { return; }

        int amount = (int) entity.fluidStorage.amount;
        int level = 0;
        if(amount == 0) { level = 0; }

        matrices.push();
        ItemStack stack = ItemStack.EMPTY;
        if(entity.isComposting()) { stack = ModItems.BLUE_CLEMATITE_ESSENCE.getDefaultStack(); }
        if(entity.isInfusing()) { stack = ModItems.ETHER_LILY.getDefaultStack(); }

        if(!stack.isEmpty()) {

            double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
            matrices.translate(0.5, 1.15 + offset/2, 0.5);

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion( renderTime / 10 ));

            int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
        }
        matrices.pop();

    }
}