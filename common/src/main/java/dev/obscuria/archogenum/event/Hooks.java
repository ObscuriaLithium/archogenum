package dev.obscuria.archogenum.event;

import dev.obscuria.archogenum.registry.ArchoAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class Hooks {

    public static float modifyExhaustion(Player player, float value) {
        final var rate = player.getAttributeValue(ArchoAttributes.HUNGER_RATE.get());
        return value * (float) rate;
    }

    public static int modifyRespiration(LivingEntity entity, int value) {
        final var efficiency = entity.getAttributeValue(ArchoAttributes.RESPIRATION_EFFICIENCY.get());
        return efficiency > 1.0 ? value + (int) Math.floor(efficiency - 1.0) : value;
    }
}
