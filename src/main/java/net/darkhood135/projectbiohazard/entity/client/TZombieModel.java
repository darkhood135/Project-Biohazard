package net.darkhood135.projectbiohazard.entity.client;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.minecraft.resources.Identifier;

public class TZombieModel extends GeoModel<TZombieEntity> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "entity/t-virus_zombie");
    }

    @Override
    public Identifier getAnimationResource(TZombieEntity animatable) {
        return Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "entity/t-virus_zombie");
    }

    public static final DataTicket<Integer> VARIANT_TICKET = DataTicket.create("t_zombie_variant", Integer.class);

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        int id = renderState.getOrDefaultGeckolibData(VARIANT_TICKET, 0);
        return TZombieEntity.Variant.byId(id).texture;
    }
}