package fr.universecorp.mysticaluniverse.client.renderers;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;


@Environment(EnvType.CLIENT)
public class IEComposterFluidRenderer implements BlockEntityRenderer<IEComposterEntity> {

    private int renderTime = 0;

    public IEComposterFluidRenderer(BlockEntityRendererFactory.Context ctx) { }

    @Override
    public void render(IEComposterEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if(renderTime >= 3600) { renderTime = 0; }
        else                  { renderTime+= 1; }

        matrices.push();
        ItemStack stack = IEComposterEntity.getRenderStack(entity);

        if(!stack.isEmpty()) {

            double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
            matrices.translate(0.5, 1.15 + offset/2, 0.5);

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion( renderTime  ));

            int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
        }
        matrices.pop();

    }
}