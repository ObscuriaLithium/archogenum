package dev.obscuria.archogenum.world.genetics.resource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.fragmentum.network.PayloadCodec;

public interface GeneticProfile {

    ProfileDiscoveries discoveries();

    ProfileXenotypes xenotypes();

    ProfileBundles bundles();

    default void updateFrom(GeneticProfile profile) {
        this.discoveries().updateFrom(profile.discoveries());
        this.xenotypes().updateFrom(profile.xenotypes());
        this.bundles().updateFrom(profile.bundles());
    }

    static GeneticProfile empty() {
        return new Instance(
                ProfileDiscoveries.empty(),
                ProfileXenotypes.empty(),
                ProfileBundles.empty());
    }

    record Instance(
            ProfileDiscoveries discoveries,
            ProfileXenotypes xenotypes,
            ProfileBundles bundles
    ) implements GeneticProfile {

        public static final Codec<GeneticProfile> CODEC;
        public static final PayloadCodec<GeneticProfile> NETWORK_CODEC;

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    ProfileDiscoveries.CODEC.fieldOf("discoveries").forGetter(GeneticProfile::discoveries),
                    ProfileXenotypes.CODEC.fieldOf("xenotypes").forGetter(GeneticProfile::xenotypes),
                    ProfileBundles.CODEC.fieldOf("bundles").forGetter(GeneticProfile::bundles)
            ).apply(codec, Instance::new));
            NETWORK_CODEC = PayloadCodec.registryFriendly(CODEC, ArchogenumProxy::registryAccess);
        }
    }
}
