package dev.obscuria.archogenum.world.genetics.trait.triggers;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public interface TriggerParams {

    LootContextParamSet DEFAULT = LootContextParamSet.builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .build();

    LootContextParamSet DAMAGE = LootContextParamSet.builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.DAMAGE_SOURCE)
            .optional(LootContextParams.DIRECT_KILLER_ENTITY)
            .optional(LootContextParams.KILLER_ENTITY)
            .build();
}
