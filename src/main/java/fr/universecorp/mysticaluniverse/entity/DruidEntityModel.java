package fr.universecorp.mysticaluniverse.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;

public class DruidEntityModel extends EntityModel<DruidEntity> {

    private final ModelPart base;

    public DruidEntityModel(ModelPart modelPart) {
        this.base = modelPart.getChild(EntityModelPartNames.ROOT);
    }

    public static TexturedModelData getTextureModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild(EntityModelPartNames.ROOT, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));

        root.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                                         .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)),
                ModelTransform.pivot(0F, 0F, 0F));

        root.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create().uv(16, 16)
                                .cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F),
                ModelTransform.pivot(0F, 0F, 0F));

        root.addChild(EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create().uv(40, 16)
                                .cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F).mirrored(false),
                ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        root.addChild(EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create().uv(40, 16)
                                .cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        root.addChild(EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create().uv(0, 16)
                                         .cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F).mirrored(false),
                ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        root.addChild(EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create().uv(0, 16)
                                .cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(DruidEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.base).forEach((modelRenderer) -> modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha));
    }
}
