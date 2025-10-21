package dev.obscuria.archogenum.world.genetics.trait.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.ValidationContext;

import java.util.List;
import java.util.function.Function;

public interface CompositeCondition extends ICondition {

    List<ICondition> terms();

    @Override
    default void validate(ValidationContext context) {
        ICondition.super.validate(context);
        terms().forEach(term -> {
            final var index = terms().indexOf(term);
            term.validate(context.forChild(".term[%s]".formatted(index)));
        });
    }

    static <T extends CompositeCondition> Codec<T> createCodec(Function<List<ICondition>, T> builder) {
        return RecordCodecBuilder.create(codec -> codec.group(
                ICondition.CODEC.listOf().fieldOf("terms").forGetter(CompositeCondition::terms)
        ).apply(codec, builder));
    }

    static <T extends CompositeCondition> Codec<T> createInlineCodec(Function<List<ICondition>, T> builder) {
        return ICondition.CODEC.listOf().xmap(builder, CompositeCondition::terms);
    }
}
