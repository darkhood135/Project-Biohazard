package net.darkhood135.projectbiohazard.item.client;

import com.geckolib.renderer.GeoItemRenderer;
import net.darkhood135.projectbiohazard.item.custom.FirstAidSprayItem;

public class FirstAidSprayRenderer extends GeoItemRenderer<FirstAidSprayItem> {
    public FirstAidSprayRenderer() {
        super(new FirstAidSprayModel());
    }
}