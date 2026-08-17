package net.darkhood135.projectbiohazard.item.client;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.item.custom.FirstAidSprayItem;
import net.minecraft.resources.Identifier;

public class FirstAidSprayModel extends GeoModel<FirstAidSprayItem> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "item/first_aid_spray");
    }
    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "textures/item/first_aid_spray.png");
    }
    @Override
    public Identifier getAnimationResource(FirstAidSprayItem animatable) {
        return Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "item/first_aid_spray");
    }
}