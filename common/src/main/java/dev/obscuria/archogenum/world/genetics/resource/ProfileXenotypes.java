package dev.obscuria.archogenum.world.genetics.resource;

import com.mojang.serialization.Codec;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.fragmentum.util.signal.Signal0;

import java.util.ArrayList;
import java.util.List;

public record ProfileXenotypes(
        List<Xenotype> xenotypes,
        Signal0 modified
) {

    public static final Codec<ProfileXenotypes> CODEC;

    public static ProfileXenotypes empty() {
        return new ProfileXenotypes(new ArrayList<>(), new Signal0());
    }

    public static ProfileXenotypes load(List<Xenotype> xenotypes) {
        return new ProfileXenotypes(new ArrayList<>(xenotypes), new Signal0());
    }

    public boolean add(Xenotype xenotype) {
        if (xenotype.isEmpty() || xenotypes.contains(xenotype)) return false;
        xenotypes.add(xenotype);
        modified.emit();
        return true;
    }

    public boolean remove(Xenotype xenotype) {
        if (xenotype.isEmpty() || !xenotypes.contains(xenotype)) return false;
        xenotypes.remove(xenotype);
        modified.emit();
        return true;
    }

    public void updateFrom(ProfileXenotypes other) {
        xenotypes.clear();
        xenotypes.addAll(other.xenotypes);
    }

    static {
        CODEC = Codec.list(Xenotype.CODEC).xmap(ProfileXenotypes::load, ProfileXenotypes::xenotypes);
    }
}
