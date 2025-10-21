package dev.obscuria.archogenum.world.genetics.behavior;

import com.google.common.base.Charsets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.registry.ArchoAttributes;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record ModifierTrait(
        @Unmodifiable List<ModifierTemplate> modifiers
) implements ITrait {

    public static final Codec<ModifierTrait> CODEC;

    @Override
    public Codec<ModifierTrait> codec() {
        return CODEC;
    }

    @Override
    public void appendComponents(Holder<ITrait> self, Consumer<TraitComponent> consumer) {
        for (var modifier : modifiers) {
            final var amount = computeDisplayValue(modifier);
            if (amount == 0.0) continue;
            final var key = "attribute.modifier." + (amount > 0 ? "plus" : "take") + "." + modifier.operation().toValue();
            final var value = ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(amount > 0 ? amount : -amount);
            final var name = Component.translatable(modifier.attribute().value().getDescriptionId());
            final var category = pickCategory(modifier.attribute(), amount);
            consumer.accept(new TraitComponent(category, Component.translatable(key, value, name)));
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, GeneInstance gene) {
        forEachModifier(entity, (attribute, modifier) -> {
            attribute.removeModifier(modifier.uuid());
            attribute.addPermanentModifier(modifier.instantiate());
            clampHealth(entity);
        });
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, GeneInstance gene) {
        forEachModifier(entity, (attribute, modifier) -> {
            attribute.removeModifier(modifier.uuid());
            clampHealth(entity);
        });
    }

    private static double computeDisplayValue(ModifierTemplate modifier) {
        final var amount = modifier.amount();
        if (modifier.operation() == AttributeModifier.Operation.MULTIPLY_BASE) return amount * 100.0;
        if (modifier.operation() == AttributeModifier.Operation.MULTIPLY_TOTAL) return amount * 100.0;
        return modifier.attribute() == Attributes.KNOCKBACK_RESISTANCE ? amount * 10.0 : amount;
    }

    private void forEachModifier(LivingEntity entity, BiConsumer<AttributeInstance, ModifierTemplate> consumer) {
        final var attributes = entity.getAttributes();
        for (var modifier : modifiers) {
            final @Nullable var attribute = attributes.getInstance(modifier.attribute);
            if (attribute == null) continue;
            consumer.accept(attribute, modifier);
        }
    }

    private GeneCategory pickCategory(Holder<Attribute> attribute, double amount) {
        return ((amount > 0) ^ (attribute.is(ArchoAttributes.INVERSE))) ? GeneCategory.BENEFICIAL : GeneCategory.HARMFUL;
    }

    private void clampHealth(LivingEntity entity) {
        if (entity.getHealth() <= entity.getMaxHealth()) return;
        entity.setHealth(entity.getMaxHealth());
    }

    public record ModifierTemplate(
            Holder<Attribute> attribute,
            AttributeModifier.Operation operation,
            double amount
    ) {

        public static final Codec<ModifierTemplate> CODEC;

        public UUID uuid() {
            final var id = attribute.value().getDescriptionId() + ":" + operation.toString().toLowerCase();
            return UUID.nameUUIDFromBytes(id.getBytes(Charsets.UTF_8));
        }

        public AttributeModifier instantiate() {
            return new AttributeModifier(uuid(), "Gene Modifier", amount, operation);
        }

        static {
            final var operationCodec = Codec.STRING.xmap(
                    name -> AttributeModifier.Operation.valueOf(name.toUpperCase()),
                    operation -> operation.toString().toLowerCase());
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(ModifierTemplate::attribute),
                    operationCodec.fieldOf("operation").forGetter(ModifierTemplate::operation),
                    Codec.DOUBLE.fieldOf("amount").forGetter(ModifierTemplate::amount)
            ).apply(codec, ModifierTemplate::new));
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ModifierTemplate.CODEC.listOf().fieldOf("modifiers").forGetter(ModifierTrait::modifiers)
        ).apply(codec, ModifierTrait::new));
    }
}
