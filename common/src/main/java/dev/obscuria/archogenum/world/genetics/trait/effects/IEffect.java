package dev.obscuria.archogenum.world.genetics.trait.effects;

import com.mojang.serialization.Codec;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public interface IEffect {

    Codec<IEffect> CODEC = ArchoRegistries.EFFECT_TYPE.byNameCodec().dispatch(IEffect::codec, Function.identity());

    Codec<? extends IEffect> codec();

    default void apply(ServerLevel level, LivingEntity entity, GeneInstance gene) {}

    static void bootstrap(BootstrapContext<Codec<? extends IEffect>> context) {

        context.register("damage_entity", () -> DamageEntityEffect.CODEC);
        context.register("ignite", () -> IgniteEffect.CODEC);
    }
}
