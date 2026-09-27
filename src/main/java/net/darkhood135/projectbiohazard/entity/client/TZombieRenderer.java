package net.darkhood135.projectbiohazard.entity.client;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class TZombieRenderer extends GeoEntityRenderer<TZombieEntity, LivingEntityRenderState> {
    public TZombieRenderer(EntityRendererProvider.Context context) {
        super(context, new TZombieModel());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void adjustModelBonesForRender(RenderPassInfo renderPassInfo, BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);

        GeoRenderState geoState = (GeoRenderState) renderPassInfo.renderState();
        float w = geoState.getOrDefaultGeckolibData(TZombieModel.HEAD_WEIGHT_TICKET, 0f);
        if (w <= 0f) return;

        LivingEntityRenderState state = (LivingEntityRenderState) renderPassInfo.renderState();
        float headYaw   = state.yRot;
        float headPitch = state.xRot;
        snapshots.ifPresent("head", head -> head
                .setRotY(head.getRotY() - headYaw   * Mth.DEG_TO_RAD * w)
                .setRotX(head.getRotX() - headPitch * Mth.DEG_TO_RAD * w));
    }

    @Override
    protected float getDeathMaxRotation(GeoRenderState renderState) {
        return 0f;   // no vanilla flop — the death animation owns the fall
    }

    @Override
    public void addRenderData(TZombieEntity animatable, Void relatedObject,
                              LivingEntityRenderState renderState, float partialTick) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick);
        ((GeoRenderState) renderState).addGeckolibData(TZombieModel.VARIANT_TICKET, animatable.getVariant());
        ((GeoRenderState) renderState).addGeckolibData(TZombieModel.HEAD_WEIGHT_TICKET, animatable.getHeadTrackWeight());
    }

}
