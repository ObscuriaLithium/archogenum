package dev.obscuria.archogenum.world.genetics.behavior;

import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record TraitComponent(GeneCategory category, Component name) {

    public static TraitComponent beneficial(Component name) {
        return new TraitComponent(GeneCategory.BENEFICIAL, name);
    }

    public static TraitComponent harmful(Component name) {
        return new TraitComponent(GeneCategory.HARMFUL, name);
    }

    public static TraitComponent cosmetic(Component name) {
        return new TraitComponent(GeneCategory.COSMETIC, name);
    }

    public MutableComponent formatted() {
        return name.copy().withStyle(category.color);
    }

    public MutableComponent paleFormatted() {
        return name.copy().withStyle(style -> style.withColor(category.paleColor.decimal()));
    }
}
