package dev.obscuria.archogenum.world.genetics.behavior.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record EntityStateCondition(
        LootContext.EntityTarget entityToCheck,
        Optional<Boolean> canSeeSky,
        Optional<Boolean> isInWaterOrBubble,
        Optional<Boolean> isInWaterOrRain,
        Optional<Boolean> isOnFire
) implements ICondition {

    public static final Codec<EntityStateCondition> CODEC;

    @Override
    public Codec<EntityStateCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerLevel level, LootContext context) {
        final @Nullable var entity = context.getParamOrNull(entityToCheck.getParam());
        if (entity == null) return false;
        if (canSeeSky.isPresent() && canSeeSky.get() != level.canSeeSky(entity.blockPosition())) return false;
        if (isInWaterOrBubble.isPresent() && isInWaterOrBubble.get() != entity.isInWaterOrBubble()) return false;
        if (isInWaterOrRain.isPresent() && isInWaterOrRain.get() != entity.isInWaterOrRain()) return false;
        if (isOnFire.isPresent() && isOnFire.get() != entity.isOnFire()) return false;
        return true;
    }

    static {
        final var targetCodec = Codec.STRING.xmap(
                name -> LootContext.EntityTarget.getByName(name.toLowerCase()),
                value -> value.name().toLowerCase());
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                targetCodec.fieldOf("entity_to_check").forGetter(EntityStateCondition::entityToCheck),
                Codec.BOOL.optionalFieldOf("can_see_sky").forGetter(EntityStateCondition::canSeeSky),
                Codec.BOOL.optionalFieldOf("is_in_water_or_bubble").forGetter(EntityStateCondition::isInWaterOrBubble),
                Codec.BOOL.optionalFieldOf("is_in_water_or_rain").forGetter(EntityStateCondition::isInWaterOrRain),
                Codec.BOOL.optionalFieldOf("is_on_fire").forGetter(EntityStateCondition::isOnFire)
        ).apply(codec, EntityStateCondition::new));
    }
}
