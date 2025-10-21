package dev.obscuria.archogenum.world.genetics.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.basis.GeneCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.function.Consumer;

public record DamageExposureTrait(
        @Unmodifiable List<Entry> entries
) implements ITrait {

    public static final Codec<DamageExposureTrait> CODEC;

    @Override
    public Codec<DamageExposureTrait> codec() {
        return CODEC;
    }

    @Override
    public double exposureTo(DamageSource source) {
        return entries.stream().mapToDouble(it -> it.exposureTo(source)).sum();
    }

    @Override
    public void appendComponents(Holder<ITrait> self, Consumer<TraitComponent> consumer) {
        entries.stream().map(Entry::component).forEach(consumer);
    }

    public enum Type implements StringRepresentable {
        RESISTANCE(GeneCategory.BENEFICIAL),
        VULNERABILITY(GeneCategory.HARMFUL);

        public final GeneCategory category;

        Type(GeneCategory category) {
            this.category = category;
        }

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }

    public record Entry(
            Type type,
            ResourceKey<DamageType> damageType,
            double amount
    ) {

        public static final Codec<Entry> CODEC;

        public double exposureTo(DamageSource source) {
            if (!source.is(damageType)) return 0.0;
            return type == Type.RESISTANCE ? -amount : amount;
        }

        public TraitComponent component() {
            final var value = "+" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(amount * 100.0) + "%";
            final var name = Component.translatable(damageType.location().toLanguageKey("damage_type"));
            final var result = Component.translatable("exposure." + type.getSerializedName(), value, name);
            return new TraitComponent(type.category, result);
        }

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    Type.CODEC.fieldOf("type").forGetter(Entry::type),
                    ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage_type").forGetter(Entry::damageType),
                    Codec.DOUBLE.fieldOf("amount").forGetter(Entry::amount)
            ).apply(codec, Entry::new));
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Entry.CODEC.listOf().fieldOf("entries").forGetter(DamageExposureTrait::entries)
        ).apply(codec, DamageExposureTrait::new));
    }
}
