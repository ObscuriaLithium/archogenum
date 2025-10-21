package dev.obscuria.archogenum.world.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public final class PercentAttribute extends RangedAttribute {

    public PercentAttribute(String descriptionId, double base, double min, double max) {
        super(descriptionId, base, min, max);
    }
}
