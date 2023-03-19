package fr.universecorp.mysticaluniverse.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

import java.security.Key;

@Environment(EnvType.CLIENT)
public class DruidAnimations {

    public static final Animation ATTACKING = Animation.Builder.create(2.54167f).looping()
            .addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0f, AnimationHelper.createRotationalVector(0.15059f, 0.85481f, -0.01753f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5f, AnimationHelper.createRotationalVector(-16.3184f, -7.6148f, -127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.8333f, AnimationHelper.createRotationalVector(-5.2207f, -7.6148f, -127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.1667f, AnimationHelper.createRotationalVector(-16.3184f, -7.6148f, -127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.5f, AnimationHelper.createRotationalVector(-5.2207f, -7.6148f, -127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.8333f, AnimationHelper.createRotationalVector(-16.3184f, -7.6148f, -127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(2.2917f, AnimationHelper.createRotationalVector(0.15059f, 0.85481f, -0.01753f), Transformation.Interpolations.CUBIC)))

            .addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0f, AnimationHelper.createRotationalVector(0.15059f, 0.85481f, -0.01753f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.5f, AnimationHelper.createRotationalVector(-16.31842f, 7.61484f, 127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(0.8333f, AnimationHelper.createRotationalVector(-5.2207f, 7.6148f, 127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.1667f, AnimationHelper.createRotationalVector(-16.31842f, 7.61484f, 127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.5f, AnimationHelper.createRotationalVector(-5.2207f, 7.6148f, 127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(1.8333f, AnimationHelper.createRotationalVector(-16.31842f, 7.61484f, 127.8128f), Transformation.Interpolations.CUBIC),
                    new Keyframe(2.2917f, AnimationHelper.createRotationalVector(0.15059f, 0.85481f, -0.01753f), Transformation.Interpolations.CUBIC)

            )).build();
}
