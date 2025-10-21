package dev.obscuria.archogenum.world.item;

import dev.obscuria.fragmentum.registry.Deferred;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

public class MusicDiscItem extends RecordItem {

    public MusicDiscItem(int analogOutput, Deferred<SoundEvent, SoundEvent> sound, Properties properties, int lengthInSeconds) {
        super(analogOutput, sound.get(), properties, lengthInSeconds);
    }
}
