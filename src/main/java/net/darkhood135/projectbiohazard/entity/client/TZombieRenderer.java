package net.darkhood135.projectbiohazard.entity.client;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class TZombieRenderer extends GeoEntityRenderer<TZombieEntity, LivingEntityRenderState> {
    public TZombieRenderer(EntityRendererProvider.Context context) {
        super(context, new TZombieModel());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void adjustModelBonesForRender(RenderPassInfo renderPassInfo, BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);

        LivingEntityRenderState state = (LivingEntityRenderState) renderPassInfo.renderState();
        float headYaw   = state.yRot;
        float headPitch = state.xRot;

        snapshots.ifPresent("head", head -> head
                .setRotY(head.getRotY() - headYaw   * Mth.DEG_TO_RAD)   // was +, now -
                .setRotX(head.getRotX() - headPitch * Mth.DEG_TO_RAD));
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
    }

}
