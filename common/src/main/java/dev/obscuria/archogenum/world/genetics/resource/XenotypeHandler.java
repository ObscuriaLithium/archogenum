package dev.obscuria.archogenum.world.genetics.resource;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.world.enchantment.EchoGraspEnchantment;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.trait.LootDropper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.function.Consumer;

public interface XenotypeHandler {

    static Xenotype getXenotype(LivingEntity entity) {
        return ((ILivingExtension)entity).archo$getXenotype();
    }

    static void setXenotype(LivingEntity entity, Xenotype xenotype) {
        final var current = getXenotype(entity);
        onRemove(entity, current);
        ((ILivingExtension)entity).archo$setXenotype(xenotype);
        onSet(entity, xenotype);
    }

    static void tick(LivingEntity entity) {
        entity.level().getProfiler().push("xenotypeTick");
        getXenotype(entity).tick(entity);
        entity.level().getProfiler().pop();
    }

    static float modifyHurtAmount(LivingEntity entity, DamageSource source, float amount) {
        final var exposure = getXenotype(entity).exposureTo(source);
        if (exposure == 0.0) return amount;
        return Math.max(0f, amount + amount * (float) exposure);
    }

    static void dropLoot(LivingEntity entity, DamageSource source, LootParams params, Long seed, LootDropper dropper) {
        EchoGraspEnchantment.maybeDropVessel(entity, source, dropper);
        getXenotype(entity).dropLoot(entity, params, seed, dropper);
    }

    static boolean isInvulnerableTo(LivingEntity entity, DamageSource source) {
        return getXenotype(entity).isInvulnerableTo(entity, source);
    }

    static boolean canStandOnFluid(LivingEntity entity, FluidState state) {
        return getXenotype(entity).canStandOnFluid(entity, state);
    }

    static void applyPostEffect(LivingEntity entity, Consumer<ResourceLocation> consumer) {
        getXenotype(entity).applyPostEffect(entity, consumer);
    }

    static void save(LivingEntity entity, CompoundTag compound) {
        final var root = compound.getCompound(Archogenum.DISPLAY_NAME);
        root.put(Xenotype.TAG_NAME, Xenotype.writeToTag(getXenotype(entity)));
        compound.put(Archogenum.DISPLAY_NAME, root);
    }

    static void load(LivingEntity entity, CompoundTag compound) {
        final var root = compound.getCompound(Archogenum.DISPLAY_NAME);
        if (root.contains(Xenotype.TAG_NAME)) setXenotype(entity, Xenotype.readFromTag(root.get(Xenotype.TAG_NAME)));
    }

    static void restore(LivingEntity from, LivingEntity to) {
        setXenotype(to, getXenotype(from));
    }

    private static void onSet(LivingEntity entity, Xenotype xenotype) {
        xenotype.addAttributeModifiers(entity);
    }

    private static void onRemove(LivingEntity entity, Xenotype xenotype) {
        xenotype.removeAttributeModifiers(entity);
    }
}
