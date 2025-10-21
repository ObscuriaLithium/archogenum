package dev.obscuria.archogenum;

import com.google.common.base.Preconditions;
import dev.obscuria.archogenum.server.ServerGenetics;
import dev.obscuria.fragmentum.Fragmentum;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ArchogenumProxy {

    private static @Nullable MinecraftServer server = null;

    public static MinecraftServer server() {
        return Objects.requireNonNull(server);
    }

    public static RegistryAccess registryAccess() {

        if (server != null) return server.registryAccess();
        Preconditions.checkState(Fragmentum.PLATFORM.isClient(), "Can't reach server RegistryAccess");

        final @Nullable var integratedServer = Minecraft.getInstance().getSingleplayerServer();
        if (integratedServer != null) return integratedServer.registryAccess();

        final @Nullable var connection = Minecraft.getInstance().getConnection();
        Preconditions.checkNotNull(connection, "Can't reach client RegistryAccess");
        return connection.registryAccess();
    }

    public static void onServerStart(MinecraftServer server) {
        ArchogenumProxy.server = server;
    }

    public static void onServerLoad(MinecraftServer server) {
        ServerGenetics.INSTANCE.load();
    }

    public static void onServerSave(MinecraftServer server) {
        ServerGenetics.INSTANCE.save();
    }

    public static void onServerStop(MinecraftServer server) {
        ArchogenumProxy.server = null;
    }

    public static void onPlayerJoin(ServerPlayer player) {
        ServerGenetics.INSTANCE.onPlayerJoin(player);
    }

    public static void onPlayerLeave(ServerPlayer player) {
        ServerGenetics.INSTANCE.onPlayerLeave(player);
    }
}
