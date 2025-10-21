package dev.obscuria.archogenum.world.genetics.behavior.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public record EntityArmorCondition(
        LootContext.EntityTarget entityToCheck,
        Optional<Boolean> wearsAnyHelmet,
        Optional<Boolean> wearsAnyChestplate,
        Optional<Boolean> wearsAnyLeggings,
        Optional<Boolean> wearsAnyBoots
) implements ICondition {

    public static final Codec<EntityArmorCondition> CODEC;

    @Override
    public Codec<EntityArmorCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerLevel level, LootContext context) {
        if (!(context.getParamOrNull(entityToCheck.getParam()) instanceof LivingEntity entity)) return false;
        if (wearsAnyHelmet.isPresent() && wearsAnyHelmet.get() != entity.hasItemInSlot(EquipmentSlot.HEAD)) return false;
        if (wearsAnyChestplate.isPresent() && wearsAnyChestplate.get() != entity.hasItemInSlot(EquipmentSlot.CHEST)) return false;
        if (wearsAnyLeggings.isPresent() && wearsAnyLeggings.get() != entity.hasItemInSlot(EquipmentSlot.LEGS)) return false;
        if (wearsAnyBoots.isPresent() && wearsAnyBoots.get() != entity.hasItemInSlot(EquipmentSlot.FEET)) return false;
        return true;
    }

    static {
        final var targetCodec = Codec.STRING.xmap(
                name -> LootContext.EntityTarget.getByName(name.toLowerCase()),
                value -> value.name().toLowerCase());
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                targetCodec.fieldOf("entity_to_check").forGetter(EntityArmorCondition::entityToCheck),
                Codec.BOOL.optionalFieldOf("wears_any_helmet").forGetter(EntityArmorCondition::wearsAnyHelmet),
                Codec.BOOL.optionalFieldOf("wears_any_chestplate").forGetter(EntityArmorCondition::wearsAnyChestplate),
                Codec.BOOL.optionalFieldOf("wears_any_leggings").forGetter(EntityArmorCondition::wearsAnyLeggings),
                Codec.BOOL.optionalFieldOf("wears_any_boots").forGetter(EntityArmorCondition::wearsAnyBoots)
        ).apply(codec, EntityArmorCondition::new));
    }
}
