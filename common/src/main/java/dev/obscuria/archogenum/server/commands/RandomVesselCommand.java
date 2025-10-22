package dev.obscuria.archogenum.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.registry.ArchoItems;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;

public final class RandomVesselCommand {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext registryAccess,
            Commands.CommandSelection environment
    ) {
        dispatcher.register(Commands.literal(Archogenum.MODID)
                .then(Commands.literal("makeRandomVessel")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> makeRandomVessel(
                                        context.getSource(),
                                        EntityArgument.getPlayer(context, "player")
                                )))));
    }

    private static int makeRandomVessel(CommandSourceStack source, ServerPlayer player) {

        final var result = new ArrayList<GeneInstance>();

        final var amount = player.getRandom().nextIntBetweenInclusive(2, 3);
        final var lookup = source.registryAccess().lookupOrThrow(ArchoRegistries.Keys.GENE);
        final var elements = new ArrayList<>(lookup.listElements().toList());

        for (var i = 0; i < amount; i++) {
            final var gene = elements.remove(player.getRandom().nextIntBetweenInclusive(0, elements.size() - 1));
            result.add(new GeneInstance(gene, 1));
        }

        final var vessel = ArchoItems.ECHO_VESSEL.instantiate();
        EchoVesselItem.setStoredGenes(vessel, new StoredGenes(result));
        if (!player.addItem(vessel)) player.drop(vessel, false);
        return 1;
    }
}
