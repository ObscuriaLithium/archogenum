package dev.obscuria.archogenum.world.genetics.behavior;

import com.mojang.serialization.Codec;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.fragmentum.registry.LazyHolder;
import dev.obscuria.fragmentum.registry.RegistryLazyCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ITrait {

    Codec<Holder<ITrait>> CODEC = RegistryFixedCodec.create(ArchoRegistries.Keys.TRAIT);
    Codec<LazyHolder<ITrait>> LAZY_CODEC = RegistryLazyCodec.create(ArchoRegistries.Keys.TRAIT, ArchogenumProxy::registryAccess);
    Codec<ITrait> DIRECT_CODEC = ArchoRegistries.TRAIT_TYPE.byNameCodec().dispatch(ITrait::codec, Function.identity());

    Codec<? extends ITrait> codec();

    default void appendComponents(Holder<ITrait> self, Consumer<TraitComponent> consumer) {}

    default void tick(ServerLevel level, LivingEntity entity, GeneInstance gene) {}

    default void addAttributeModifiers(LivingEntity entity, GeneInstance gene) {}

    default void removeAttributeModifiers(LivingEntity entity, GeneInstance gene) {}

    default void dropLoot(LivingEntity entity, LootParams params, long seed, LootDropper dropper) {}

    static void bootstrap(BootstrapContext<Codec<? extends ITrait>> context) {

        context.register("behavior", () -> BehaviorTrait.CODEC);
        context.register("extra_drops", () -> ExtraDropTrait.CODEC);
        context.register("modifiers", () -> ModifierTrait.CODEC);
        context.register("effects", () -> EffectTrait.CODEC);
        context.register("cosmetic", () -> CosmeticTrait.CODEC);
        context.register("custom_tag", () -> CustomTagTrait.CODEC);
    }
}
