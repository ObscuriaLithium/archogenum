package dev.obscuria.archogenum.client;

import dev.obscuria.archogenum.world.genetics.resource.GeneticProfile;
import dev.obscuria.archogenum.world.genetics.resource.Genetics;
import net.minecraft.world.entity.player.Player;

public final class ClientGenetics implements Genetics {

    public static final ClientGenetics INSTANCE = new ClientGenetics();

    @Override
    public GeneticProfile profileOf(Player player) {
        return ClientGeneticProfile.INSTANCE;
    }
}
