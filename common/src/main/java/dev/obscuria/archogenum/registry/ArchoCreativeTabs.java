package dev.obscuria.archogenum.registry;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.world.genetics.GeneInstance;
import dev.obscuria.archogenum.world.genetics.StoredGenes;
import dev.obscuria.archogenum.world.item.EchoVesselItem;
import dev.obscuria.fragmentum.registry.Deferred;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.List;
import java.util.function.Supplier;

public interface ArchoCreativeTabs {

    Deferred<CreativeModeTab, CreativeModeTab> ARCHOGENUM = create("archogenum", () -> CreativeModeTab
            .builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable("itemGroup.archogenum"))
            .icon(ArchoItems.XENOFRUIT::instantiate)
            .displayItems(ArchoCreativeTabs::buildItems)
            .build());

    private static Deferred<CreativeModeTab, CreativeModeTab> create(String name, Supplier<CreativeModeTab> value) {
        return ArchoRegistries.REGISTRAR.register(Registries.CREATIVE_MODE_TAB, Archogenum.key(name), value);
    }

    private static void buildItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        output.accept(ArchoItems.ECHO_FRUIT);
        output.accept(ArchoItems.XENOFRUIT);
        output.accept(ArchoItems.MUSIC_DISC);
        params.holders().lookupOrThrow(ArchoRegistries.Keys.GENE).listElements().forEach(gene -> {
            final var stack = ArchoItems.ECHO_VESSEL.instantiate();
            final var storedGenes = new StoredGenes(List.of(new GeneInstance(gene, 1)));
            EchoVesselItem.setStoredGenes(stack, storedGenes);
            output.accept(stack);
        });
    }

    static void init() {}
}
