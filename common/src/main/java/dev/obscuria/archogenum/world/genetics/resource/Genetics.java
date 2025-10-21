package dev.obscuria.archogenum.world.genetics.resource;

import net.minecraft.world.entity.player.Player;

public interface Genetics {

    GeneticProfile profileOf(Player player);
}
