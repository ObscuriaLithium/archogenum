package dev.obscuria.archogenum.registry;

import com.google.common.base.Suppliers;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.fragmentum.registry.Deferred;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public interface ArchoSounds {

    Deferred<SoundEvent, SoundEvent> BACKGROUND = create("music.background");
    Deferred<SoundEvent, SoundEvent> MUSIC_DISC = create("music.music_disc");
    Deferred<SoundEvent, SoundEvent> UI_BELL = create("ui.bell");
    Deferred<SoundEvent, SoundEvent> UI_CLICK_1 = create("ui.click_1");
    Deferred<SoundEvent, SoundEvent> UI_CLICK_2 = create("ui.click_2");
    Deferred<SoundEvent, SoundEvent> UI_SCROLL = create("ui.scroll");

    Supplier<Music> MUSIC_BACKGROUND = music(BACKGROUND);

    private static Deferred<SoundEvent, SoundEvent> create(String name) {
        return ArchoRegistries.REGISTRAR.register(
                Registries.SOUND_EVENT,
                Archogenum.key(name),
                () -> SoundEvent.createVariableRangeEvent(Archogenum.key(name)));
    }

    private static Supplier<Music> music(Deferred<SoundEvent, SoundEvent> sound) {
        return Suppliers.memoize(() -> new Music(sound.holder(), 600, 3000, false));
    }

    static void init() {}
}
