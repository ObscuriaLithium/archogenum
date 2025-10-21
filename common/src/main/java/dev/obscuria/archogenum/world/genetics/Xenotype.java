package dev.obscuria.archogenum.world.genetics;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import dev.obscuria.archogenum.world.genetics.trait.CustomTagTrait;
import dev.obscuria.archogenum.world.genetics.trait.LootDropper;
import dev.obscuria.archogenum.world.genetics.resource.GeneBundle;
import dev.obscuria.fragmentum.network.PayloadCodec;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public record Xenotype(
        @Unmodifiable List<GeneInstance> genes
) implements IBundleLike {

    public static final Xenotype EMPTY = new Xenotype(List.of());
    public static final String TAG_NAME = "Xenotype";
    public static final Codec<Xenotype> CODEC;
    public static final PayloadCodec<Xenotype> NETWORK_CODEC;

    public int complexity() {
        return genes.stream().mapToInt(it -> it.gene().value().complexity()).sum();
    }

    public boolean isEmpty() {
        return genes.isEmpty();
    }

    public Xenotype with(GeneInstance gene) {
        if (genes.contains(gene)) return this;
        return new Xenotype(Stream.concat(genes.stream(), Stream.of(gene)).toList());
    }

    public Xenotype without(GeneInstance gene) {
        if (!genes.contains(gene)) return this;
        return new Xenotype(genes.stream().filter(it -> !it.equals(gene)).toList());
    }

    public boolean hasGene(Holder<Gene> gene) {
        for (var instance : genes()) {
            if (!instance.gene().equals(gene)) continue;
            return true;
        }
        return false;
    }

    public void tick(LivingEntity entity) {
        for (var gene : genes) {
            gene.tick(entity);
        }
    }

    public void addAttributeModifiers(LivingEntity entity) {
        for (var gene : genes) {
            gene.addAttributeModifiers(entity);
        }
    }

    public void removeAttributeModifiers(LivingEntity entity) {
        for (var gene : genes) {
            gene.removeAttributeModifiers(entity);
        }
    }

    public void dropLoot(LivingEntity entity, LootParams params, Long seed, LootDropper dropper) {
        for (var gene : genes) {
            gene.dropLoot(entity, params, seed, dropper);
        }
    }

    public double exposureTo(DamageSource source) {
        var result = 0.0;
        for (var gene : genes) {
            result += gene.exposureTo(source);
        }
        return result;
    }

    public boolean isInvulnerableTo(LivingEntity entity, DamageSource source) {
        for (var gene : genes) {
            if (!gene.isInvulnerableTo(entity, source)) continue;
            return true;
        }
        return false;
    }

    public boolean canStandOnFluid(LivingEntity entity, FluidState state) {
        for (var gene : genes) {
            if (!gene.canStandOnFluid(entity, state)) continue;
            return true;
        }
        return false;
    }

    public boolean hasArchogene() {
        return genes().stream().anyMatch(it -> it.gene().value().isArchogene());
    }

    public boolean hasCustomTag(ResourceLocation tag) {
        return genes().stream()
                .flatMap(it -> it.gene().value().listTraits())
                .anyMatch(it -> it.value() instanceof CustomTagTrait trait && trait.tag().equals(tag));
    }

    public GeneBundle toBundle() {
        return new GeneBundle(genes);
    }

    public Xenotype copy() {
        return new Xenotype(List.copyOf(genes));
    }

    public static Tag writeToTag(Xenotype xenotype) {
        final var ops = RegistryOps.create(NbtOps.INSTANCE, ArchogenumProxy.registryAccess());
        return CODEC.encodeStart(ops, xenotype).result().orElseGet(CompoundTag::new);
    }

    public static Xenotype readFromTag(@Nullable Tag tag) {
        if (tag == null) return EMPTY;
        final var ops = RegistryOps.create(NbtOps.INSTANCE, ArchogenumProxy.registryAccess());
        return CODEC.decode(ops, tag).result().map(Pair::getFirst).orElse(EMPTY);
    }

    public static Xenotype assemble(List<IBundleLike> bundles) {
        final var genes = new HashSet<GeneInstance>();
        bundles.stream()
                .flatMap(bundle -> bundle.genes().stream())
                .forEach(genes::add);
        return new Xenotype(List.copyOf(genes));
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                GeneInstance.CODEC.listOf().fieldOf("genes").forGetter(Xenotype::genes)
        ).apply(codec, Xenotype::new));
        NETWORK_CODEC = PayloadCodec.registryFriendly(CODEC, ArchogenumProxy::registryAccess);
    }
}
