package net.darkhood135.projectbiohazard.entity.client;

import com.geckolib.renderer.GeoEntityRenderer;
import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class TZombieRenderer extends GeoEntityRenderer<TZombieEntity, LivingEntityRenderState> {
    public TZombieRenderer(EntityRendererProvider.Context context) {
        super(context, new TZombieModel());
    }
}
