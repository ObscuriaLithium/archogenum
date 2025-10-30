
local secondsOnFire = 8

local function shouldIgnite(entity)
    return (entity.tickCount % 20 == 0)
            and entity.level:isDay()
            and entity:canSeeSky()
            and entity:getHelmet():isEmpty()
            and not entity:isInWaterOrRain()
end

function tick(entity)
    if shouldIgnite(entity) then
        entity:ignite(secondsOnFire)
    end
end