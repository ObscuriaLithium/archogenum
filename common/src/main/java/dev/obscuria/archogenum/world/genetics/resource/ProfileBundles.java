package dev.obscuria.archogenum.world.genetics.resource;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.util.signal.Signal0;

import java.util.ArrayList;
import java.util.List;

public record ProfileBundles(
        List<GeneBundle> bundles,
        Signal0 modified
) {

    public static final Codec<ProfileBundles> CODEC;

    public static ProfileBundles empty() {
        return new ProfileBundles(new ArrayList<>(), new Signal0());
    }

    public static ProfileBundles load(List<GeneBundle> bundles) {
        return new ProfileBundles(new ArrayList<>(bundles), new Signal0());
    }

    public boolean add(GeneBundle bundle) {
        if (bundle.isEmpty() || bundles.contains(bundle)) return false;
        bundles.add(bundle);
        modified.emit();
        return true;
    }

    public boolean remove(GeneBundle bundle) {
        if (bundle.isEmpty() || !bundles.contains(bundle)) return false;
        bundles.remove(bundle);
        modified.emit();
        return true;
    }

    public void updateFrom(ProfileBundles other) {
        bundles.clear();
        bundles.addAll(other.bundles);
    }

    static {
        CODEC = GeneBundle.CODEC.listOf().xmap(ProfileBundles::load, ProfileBundles::bundles);
    }
}
