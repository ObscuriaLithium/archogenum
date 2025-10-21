package dev.obscuria.archogenum.world.genetics.behavior.triggers;

import com.mojang.serialization.Codec;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Function;

public interface ITrigger {

    Codec<ITrigger> CODEC = ArchoRegistries.TRIGGER_TYPE.byNameCodec().dispatch(ITrigger::codec, Function.identity());

    Codec<? extends ITrigger> codec();

    default void tick(ServerLevel level, LivingEntity entity, GeneInstance gene) {}

    static LootContext defaultContext(ServerLevel level, LivingEntity entity) {
        final var params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .create(TriggerParams.DEFAULT);
        return new LootContext.Builder(params).create(null);
    }

    static void bootstrap(BootstrapContext<Codec<? extends ITrigger>> context) {

        context.register("tick", () -> TickTrigger.CODEC);
    }
}
