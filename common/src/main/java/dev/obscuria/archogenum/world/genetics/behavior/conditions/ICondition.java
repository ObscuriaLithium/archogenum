package dev.obscuria.archogenum.world.genetics.behavior.conditions;

import com.mojang.serialization.Codec;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

import java.util.function.BiPredicate;
import java.util.function.Function;

public interface ICondition extends LootContextUser, BiPredicate<ServerLevel, LootContext> {

    Codec<ICondition> CODEC = ArchoRegistries.CONDITION_TYPE.byNameCodec().dispatch("condition", ICondition::codec, Function.identity());

    Codec<? extends ICondition> codec();

    static void bootstrap(BootstrapContext<Codec<? extends ICondition>> context) {

        context.register("all_of", () -> AllOfCondition.CODEC);
        context.register("any_of", () -> AnyOfCondition.CODEC);
        context.register("none_of", () -> NoneOfCondition.CODEC);
        context.register("world_state", () -> WorldStateCondition.CODEC);
        context.register("entity_state", () -> EntityStateCondition.CODEC);
        context.register("entity_armor", () -> EntityArmorCondition.CODEC);
    }
}
