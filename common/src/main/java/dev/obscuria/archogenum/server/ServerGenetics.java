package dev.obscuria.archogenum.server;

import com.google.common.collect.Maps;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.world.genetics.resource.GeneticProfile;
import dev.obscuria.archogenum.world.genetics.resource.Genetics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public final class ServerGenetics implements Genetics {

    public static final ServerGenetics INSTANCE = new ServerGenetics();
    private static final Map<UUID, ServerGeneticProfile> PROFILES = Maps.newHashMap();
    private static final LevelResource RESOURCE;

    @Override
    public GeneticProfile profileOf(Player player) {
        return PROFILES.computeIfAbsent(player.getUUID(), ServerGenetics::loadProfile);
    }

    public void save() {
        PROFILES.values().forEach(profile -> profile.save(rootPath()));
    }

    public void load() {}

    public void onPlayerJoin(ServerPlayer player) {
        profileOf(player); // force profile loading
    }

    public void onPlayerLeave(ServerPlayer player) {
        final var profile = PROFILES.remove(player.getUUID());
        if (profile != null) profile.save(rootPath());
    }

    private static Path rootPath() {
        return ArchogenumProxy.server().getWorldPath(RESOURCE);
    }

    private static ServerGeneticProfile loadProfile(UUID uuid) {
        final var profile = new ServerGeneticProfile(ArchogenumProxy.server(), uuid, GeneticProfile.empty());
        profile.load(rootPath());
        profile.sync();
        return profile;
    }

    static {
        try {
            final var constructor = LevelResource.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            RESOURCE = constructor.newInstance("obscuria/genetics");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
