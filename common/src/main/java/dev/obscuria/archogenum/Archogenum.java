package dev.obscuria.archogenum;

import dev.obscuria.archogenum.network.*;
import dev.obscuria.archogenum.registry.*;
import dev.obscuria.archogenum.server.commands.GeneCommand;
import dev.obscuria.archogenum.server.commands.RandomVesselCommand;
import dev.obscuria.archogenum.world.genetics.XenotypeSerializer;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import dev.obscuria.fragmentum.Fragmentum;
import dev.obscuria.fragmentum.network.FragmentumNetworking;
import dev.obscuria.fragmentum.server.FragmentumServerRegistry;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Archogenum {

    String MODID = "archogenum";
    String DISPLAY_NAME = "Archogenum";

    Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);
    ResourceLocation FONT = key(MODID);
    Style STYLE = Style.EMPTY.withFont(FONT);

    ResourceLocation TAG_KEEP_INVENTORY = key("keep_inventory");

    static ResourceLocation key(String name) {
        return new ResourceLocation(MODID, name);
    }

    static boolean shouldKeepInventory(Player player) {
        return XenotypeHandler.getXenotype(player).hasCustomTag(TAG_KEEP_INVENTORY);
    }

    static void init() {

        ArchoRegistries.init();

        EntityDataSerializers.registerSerializer(XenotypeSerializer.INSTANCE);

        FragmentumServerRegistry.registerCommand(GeneCommand::register);
        FragmentumServerRegistry.registerCommand(RandomVesselCommand::register);

        final var payloadRegistrar = FragmentumNetworking.registrar(MODID);
        payloadRegistrar.registerClientbound(ClientboundProfilePayload.class,
                ClientboundProfilePayload::encode,
                ClientboundProfilePayload::decode,
                ClientboundProfilePayload::handle);
        payloadRegistrar.registerClientbound(ClientboundNewVesselPayload.class,
                ClientboundNewVesselPayload::encode,
                ClientboundNewVesselPayload::decode,
                ClientboundNewVesselPayload::handle);
        payloadRegistrar.registerClientbound(ClientboundNewGenePayload.class,
                ClientboundNewGenePayload::encode,
                ClientboundNewGenePayload::decode,
                ClientboundNewGenePayload::handle);

        payloadRegistrar.registerServerbound(ServerboundSynthesizePayload.class,
                ServerboundSynthesizePayload::encode,
                ServerboundSynthesizePayload::decode,
                ServerboundSynthesizePayload::handle);
        payloadRegistrar.registerServerbound(ServerboundSaveXenotypePayload.class,
                ServerboundSaveXenotypePayload::encode,
                ServerboundSaveXenotypePayload::decode,
                ServerboundSaveXenotypePayload::handle);

        if (Fragmentum.PLATFORM.isClient()) {
            ArchogenumClient.init();
        }
    }

    interface Warn {

        String SAVE_PROFILE = "Failed to save genetic profile of {}: {}";
        String LOAD_PROFILE = "Failed to load genetic profile of {}: {}";
    }
}
