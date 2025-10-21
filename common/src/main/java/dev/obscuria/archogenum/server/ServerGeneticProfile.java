package dev.obscuria.archogenum.server;

import com.mojang.serialization.JsonOps;
import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.network.ClientboundProfilePayload;
import dev.obscuria.archogenum.world.genetics.resource.ProfileBundles;
import dev.obscuria.archogenum.world.genetics.resource.ProfileDiscoveries;
import dev.obscuria.archogenum.world.genetics.resource.GeneticProfile;
import dev.obscuria.archogenum.world.genetics.resource.ProfileXenotypes;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.UUID;

public record ServerGeneticProfile(
        MinecraftServer server,
        UUID playerUUID,
        GeneticProfile delegate
) implements GeneticProfile {

    public ServerGeneticProfile(MinecraftServer server, UUID playerUUID, GeneticProfile delegate) {
        this.server = server;
        this.playerUUID = playerUUID;
        this.delegate = delegate;

        discoveries().modified().connect(this::sync);
        xenotypes().modified().connect(this::sync);
        bundles().modified().connect(this::sync);
    }

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

    public void save(Path root) {
        final var ops = RegistryOps.create(JsonOps.INSTANCE, ArchogenumProxy.registryAccess());
        final var result = Instance.CODEC.encodeStart(ops, this);
        result.result().ifPresent(element -> FileHelper.saveJson(resolvePath(root), element));
        result.error().ifPresent(partial -> Archogenum.LOGGER.warn(Archogenum.Warn.SAVE_PROFILE, playerName(), partial.message()));
    }

    public void load(Path root) {
        final @Nullable var element = FileHelper.loadJson(resolvePath(root));
        if (element == null) return;
        final var ops = RegistryOps.create(JsonOps.INSTANCE, ArchogenumProxy.registryAccess());
        final var result = Instance.CODEC.decode(ops, element);
        result.result().ifPresent(pair -> updateFrom(pair.getFirst()));
        result.error().ifPresent(partial -> Archogenum.LOGGER.warn(Archogenum.Warn.LOAD_PROFILE, playerName(), partial.message()));
    }

    public void sync() {
        final @Nullable var player = server.getPlayerList().getPlayer(playerUUID);
        if (player == null) return;
        FragmentumNetworking.sendTo(player, new ClientboundProfilePayload(this));
    }

    private Path resolvePath(Path root) {
        return root.resolve("profiles/%s.json".formatted(playerUUID));
    }

    private String playerName() {
        final @Nullable var player = server.getPlayerList().getPlayer(playerUUID);
        if (player == null) return playerUUID.toString();
        return player.getDisplayName().getString();
    }
}
