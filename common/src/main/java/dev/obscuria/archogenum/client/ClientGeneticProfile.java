package dev.obscuria.archogenum.client;

import dev.obscuria.archogenum.world.genetics.resource.ProfileBundles;
import dev.obscuria.archogenum.world.genetics.resource.ProfileDiscoveries;
import dev.obscuria.archogenum.world.genetics.resource.GeneticProfile;
import dev.obscuria.archogenum.world.genetics.resource.ProfileXenotypes;

public record ClientGeneticProfile(
        GeneticProfile delegate
) implements GeneticProfile {

    public static final ClientGeneticProfile INSTANCE = new ClientGeneticProfile(GeneticProfile.empty());

    @Override
    public ProfileDiscoveries discoveries() {
        return delegate.discoveries();
    }

    @Override
    public ProfileXenotypes xenotypes() {
        return delegate.xenotypes();
    }

    @Override
    public ProfileBundles bundles() {
        return delegate.bundles();
    }
}
