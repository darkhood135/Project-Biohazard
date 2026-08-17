package net.darkhood135.projectbiohazard.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class ModArmPoses {
    public static final EnumProxy<HumanoidModel.ArmPose> PISTOL_AIM = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,   // twoHanded
            false,   // affectsOffhandPose
            (IArmPoseTransformer) (model, state, arm) -> {
                ModelPart armPart = model.getArm(arm);
                armPart.xRot = (float) (-Math.PI / 2.0) + model.head.xRot;  // forward at eye level, tracks pitch
                armPart.yRot = model.head.yRot;                             // tracks yaw
            }
    );


}