package fr.universecorp.mysticaluniverse.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class DruidEntityModel<E extends DruidEntity> extends SinglePartEntityModel<E> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public DruidEntityModel(ModelPart modelPart) {
        this.root = modelPart.getChild(EntityModelPartNames.ROOT);
        this.head = this.root.getChild(EntityModelPartNames.HEAD);
        this.leftLeg = this.root.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightLeg = this.root.getChild(EntityModelPartNames.RIGHT_LEG);
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
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.head.yaw = headYaw * ((float)Math.PI / 180);
        this.head.pitch = headPitch * ((float)Math.PI / 180);
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance * 0.5f;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float)Math.PI) * 1.4f * limbDistance * 0.5f;
        this.rightLeg.yaw = 0.0f;
        this.leftLeg.yaw = 0.0f;
    }
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        ModelPart leftArm = this.root.getChild(EntityModelPartNames.LEFT_ARM);
        ModelPart rightArm = this.root.getChild(EntityModelPartNames.RIGHT_ARM);
        float f = 0.5f * (float)(arm == Arm.RIGHT ? 1 : -1);
        leftArm.pivotX += f;
        leftArm.rotate(matrices);
        leftArm.pivotX -= f;

        rightArm.pivotX += f;
        rightArm.rotate(matrices);
        rightArm.pivotX -= f;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.getPart().render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public ModelPart getPart() {
        return this.root;
    }
}
