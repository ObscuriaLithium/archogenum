package dev.obscuria.archogenum.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.registry.ArchoItems;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.server.ServerGenetics;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public final class GeneCommand {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext registryAccess,
            Commands.CommandSelection environment
    ) {
        dispatcher.register(Commands.literal(Archogenum.MODID)
                .then(Commands.literal("gene")
                        .then(Commands.literal("add")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .then(Commands.argument("gene", ResourceArgument.resource(registryAccess, ArchoRegistries.Keys.GENE))
                                                .executes(context -> addGene(
                                                        context.getSource(),
                                                        EntityArgument.getEntity(context, "target"),
                                                        ResourceArgument.getResource(context, "gene", ArchoRegistries.Keys.GENE))))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .then(Commands.argument("gene", ResourceArgument.resource(registryAccess, ArchoRegistries.Keys.GENE))
                                                .executes(context -> removeGene(
                                                        context.getSource(),
                                                        EntityArgument.getEntity(context, "target"),
                                                        ResourceArgument.getResource(context, "gene", ArchoRegistries.Keys.GENE))))))
                        .then(Commands.literal("discover")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.literal("*")
                                                .executes(context -> discoverAll(
                                                        context.getSource(),
                                                        EntityArgument.getPlayer(context, "player"))))
                                        .then(Commands.argument("gene", ResourceArgument.resource(registryAccess, ArchoRegistries.Keys.GENE))
                                                .executes(context -> discoverOnly(
                                                        context.getSource(),
                                                        EntityArgument.getPlayer(context, "player"),
                                                        ResourceArgument.getResource(context, "gene", ArchoRegistries.Keys.GENE))))))
                        .then(Commands.literal("forget")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.literal("*")
                                                .executes(context -> forgetAll(
                                                        context.getSource(),
                                                        EntityArgument.getPlayer(context, "player"))))
                                        .then(Commands.argument("gene", ResourceArgument.resource(registryAccess, ArchoRegistries.Keys.GENE))
                                                .executes(context -> forgetOnly(
                                                        context.getSource(),
                                                        EntityArgument.getPlayer(context, "player"),
                                                        ResourceArgument.getResource(context, "gene", ArchoRegistries.Keys.GENE))))))
                        .then(Commands.literal("give")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("gene", ResourceArgument.resource(registryAccess, ArchoRegistries.Keys.GENE))
                                                .executes(context -> giveGene(
                                                        context.getSource(),
                                                        EntityArgument.getPlayer(context, "player"),
                                                        ResourceArgument.getResource(context, "gene", ArchoRegistries.Keys.GENE))))))));
    }

    private static int addGene(CommandSourceStack source, Entity target, Holder<Gene> gene) {
        if (!(target instanceof LivingEntity living)) return -1;
        final var instance = new GeneInstance(gene, 1);
        XenotypeHandler.setXenotype(living, XenotypeHandler.getXenotype(living).with(instance));
        return 1;
    }

    private static int removeGene(CommandSourceStack source, Entity target, Holder<Gene> gene) {
        if (!(target instanceof LivingEntity living)) return -1;
        final var instance = new GeneInstance(gene, 1);
        XenotypeHandler.setXenotype(living, XenotypeHandler.getXenotype(living).without(instance));
        return 1;
    }

    private static int discoverAll(CommandSourceStack source, ServerPlayer player) {
        final var lookup = source.registryAccess().lookupOrThrow(ArchoRegistries.Keys.GENE);
        return lookup.listElements().mapToInt(gene -> discoverOnly(source, player, gene)).sum();
    }

    private static int discoverOnly(CommandSourceStack source, ServerPlayer player, Holder<Gene> gene) {
        final var profile = ServerGenetics.INSTANCE.profileOf(player);
        return profile.discoveries().discover(player, gene) ? 1 : 0;
    }

    private static int forgetAll(CommandSourceStack source, ServerPlayer player) {
        final var lookup = source.registryAccess().lookupOrThrow(ArchoRegistries.Keys.GENE);
        return lookup.listElements().mapToInt(gene -> forgetOnly(source, player, gene)).sum();
    }

    private static int forgetOnly(CommandSourceStack source, ServerPlayer player, Holder<Gene> gene) {
        final var profile = ServerGenetics.INSTANCE.profileOf(player);
        return profile.discoveries().forget(gene) ? 1 : 0;
    }

    private static int giveGene(CommandSourceStack source, ServerPlayer player, Holder<Gene> gene) {
        final var stack = ArchoItems.ECHO_VESSEL.instantiate();
        final var storedGenes = new StoredGenes(List.of(new GeneInstance(gene, 1)));
        EchoVesselItem.setStoredGenes(stack, storedGenes);
        if (!player.addItem(stack)) {
            player.drop(stack, false);
        }
        return 1;
    }
}
