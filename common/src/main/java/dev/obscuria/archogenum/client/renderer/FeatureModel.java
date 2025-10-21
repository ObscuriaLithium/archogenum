package dev.obscuria.archogenum.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class FeatureModel<T extends LivingEntity> extends HumanoidModel<T> {

    public FeatureModel(ModelPart root) {
        super(root);
    }
}
