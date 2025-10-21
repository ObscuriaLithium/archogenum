package dev.obscuria.archogenum.registry;

import dev.obscuria.archogenum.Archogenum;
import dev.obscuria.archogenum.world.item.*;
import dev.obscuria.fragmentum.registry.DeferredItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

import java.util.function.Supplier;

public interface ArchoItems {

    DeferredItem<GeneExtractorItem> GENE_EXTRACTOR = create("gene_extractor", () -> new GeneExtractorItem(Tiers.IRON, new Item.Properties()));
    DeferredItem<EchoVesselItem> GENE_PACK = create("echo_vessel", () -> new EchoVesselItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    DeferredItem<EchoFruitItem> ECHO_FRUIT = create("echo_fruit", () -> new EchoFruitItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    DeferredItem<XenofruitItem> XENOFRUIT = create("xenofruit", () -> new XenofruitItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    DeferredItem<MusicDiscItem> MUSIC_DISC = create("music_disc", () -> new MusicDiscItem(6, ArchoSounds.MUSIC_DISC, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), 82));

    private static <T extends Item> DeferredItem<T> create(String name, Supplier<T> value) {
        return ArchoRegistries.REGISTRAR.registerItem(Archogenum.key(name), value);
    }

    static void init() {}
}
