package dev.obscuria.archogenum.world.genetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.behavior.ITrait;
import dev.obscuria.archogenum.world.genetics.behavior.LootDropper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.function.Consumer;

public record GeneInstance(
        Holder<Gene> gene,
        int expression
) {

    public static final Codec<GeneInstance> CODEC;

    public Component getDisplayName() {
        return Component.translatable("gene.instance_name_format",
                gene.value().getDisplayName(gene),
                Component.translatable("gene.expression." + expression));
    }

    public void tick(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;
        forEachTrait(trait -> trait.tick(level, entity, this));
    }

    public void addAttributeModifiers(LivingEntity entity) {
        forEachTrait(trait -> trait.addAttributeModifiers(entity, this));
    }

    public void removeAttributeModifiers(LivingEntity entity) {
        forEachTrait(trait -> trait.removeAttributeModifiers(entity, this));
    }

    public void dropLoot(LivingEntity entity, LootParams params, Long seed, LootDropper dropper) {
        forEachTrait(trait -> trait.dropLoot(entity, params, seed, dropper));
    }

    private void forEachTrait(Consumer<ITrait> consumer) {
        if (gene.value().traits().isEmpty()) return;
        for (var behavior : gene.value().traits().get()) {
            consumer.accept(behavior.getOrThrow().value());
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Gene.CODEC.fieldOf("gene").forGetter(GeneInstance::gene),
                Codec.INT.fieldOf("expression").forGetter(GeneInstance::expression)
        ).apply(codec, GeneInstance::new));
    }
}
