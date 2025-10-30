-- Damage type used for hurting the entity.
-- You can replace "minecraft:drown" with another damage type, e.g. "minecraft:fire".
local damageType = factory:resourceKeyOf("minecraft:damage_type", "minecraft:drown")
-- How much damage to apply every 10 ticks (0.5 seconds).
-- You can change this number to make damage weaker or stronger.
local damageAmount = 1

-- This function runs every game tick for the entity.
function tick(entity)
    if (entity.tickCount % 10 == 0) and entity:isInWaterRainOrBubble() then
        entity:hurt(factory:damageSourceOf(damageType, entity), damageAmount)
    end
end