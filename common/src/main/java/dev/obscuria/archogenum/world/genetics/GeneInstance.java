package dev.obscuria.archogenum.world.genetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.trait.ITrait;
import dev.obscuria.archogenum.world.genetics.trait.LootDropper;
import dev.obscuria.archogenum.world.genetics.trait.ShaderTrait;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.function.Consumer;
import java.util.function.Predicate;

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

    public double exposureTo(DamageSource source) {
        if (gene.value().traits().isEmpty()) return 0.0;
        var result = 0.0;
        for (var behavior : gene.value().traits().get()) {
            result += behavior.getOrThrow().value().exposureTo(source);
        }
        return result;
    }

    public boolean isInvulnerableTo(LivingEntity entity, DamageSource source) {
        return anyTraitMatch(trait -> trait.isInvulnerableTo(source));
    }

    public boolean canStandOnFluid(LivingEntity entity, FluidState state) {
        return anyTraitMatch(trait -> trait.canStandOnFluid(state));
    }

    public boolean applyPostEffect(LivingEntity entity, Consumer<ResourceLocation> consumer) {
        return anyTraitMatch(trait -> {
            if (!(trait instanceof ShaderTrait shader)) return false;
            consumer.accept(shader.shader());
            return true;
        });
    }

    private void forEachTrait(Consumer<ITrait> consumer) {
        if (gene.value().traits().isEmpty()) return;
        for (var behavior : gene.value().traits().get()) {
            consumer.accept(behavior.getOrThrow().value());
        }
    }

    private boolean anyTraitMatch(Predicate<ITrait> predicate) {
        if (gene.value().traits().isEmpty()) return false;
        for (var behavior : gene.value().traits().get()) {
            if (!predicate.test(behavior.getOrThrow().value())) continue;
            return true;
        }
        return false;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Gene.CODEC.fieldOf("gene").forGetter(GeneInstance::gene),
                Codec.INT.fieldOf("expression").forGetter(GeneInstance::expression)
        ).apply(codec, GeneInstance::new));
    }
}
