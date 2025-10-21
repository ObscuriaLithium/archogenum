package dev.obscuria.archogenum.registry;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.world.attribute.PercentAttribute;
import dev.obscuria.fragmentum.registry.DeferredAttribute;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public interface ArchoAttributes {

    DeferredAttribute HUNGER_RATE = percent("player.hunger_rate", 1.0, 0.0, 1024.0);
    DeferredAttribute RESPIRATION_EFFICIENCY = percent("generic.respiration_efficiency", 1.0, 0.0, 1024.0);
    DeferredAttribute FALL_DAMAGE = percent("generic.fall_damage", 1.0, 0.0, 1024.0);

    TagKey<Attribute> INVERSE = TagKey.create(Registries.ATTRIBUTE, Archogenum.key("inverse"));

    static void createPlayerAttributes(AttributeSupplier.Builder builder) {
        builder.add(HUNGER_RATE.get());
    }

    static void createLivingAttributes(AttributeSupplier.Builder builder) {
        builder.add(FALL_DAMAGE.get());
        builder.add(RESPIRATION_EFFICIENCY.get());
    }

    private static DeferredAttribute ranged(String name, double base, double min, double max) {
        return ArchoRegistries.REGISTRAR.registerAttribute(Archogenum.key(name), () -> {
            final var descriptionId = "attribute.%s.%s".formatted(Archogenum.MODID, name);
            return new RangedAttribute(descriptionId, base, min, max).setSyncable(true);
        });
    }

    private static DeferredAttribute percent(String name, double base, double min, double max) {
        return ArchoRegistries.REGISTRAR.registerAttribute(Archogenum.key(name), () -> {
            final var descriptionId = "attribute.%s.%s".formatted(Archogenum.MODID, name);
            return new PercentAttribute(descriptionId, base, min, max).setSyncable(true);
        });
    }

    static void init() {}
}
