package dev.obscuria.archogenum.client.screen.elements.details;

import dev.obscuria.archogenum.client.screen.containers.ListContainer;
import dev.obscuria.archogenum.client.screen.elements.GeneBigIcon;
import dev.obscuria.archogenum.client.screen.elements.ItemListDisplay;
import dev.obscuria.archogenum.client.screen.nodes.TextNode;
import dev.obscuria.archogenum.registry.ArchoEnchantments;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.List;

public class GeneDescription extends ListContainer {

    private static final Component UNKNOWN_DESCRIPTION;
    private static final ItemStack ENCHANTED_BOOK;
    private static final ItemStack ECHO_SHARD;

    public static GeneDescription known(Holder<Gene> gene) {
        final var result = new GeneDescription(gene);
        result.addChild(TextNode.description(gene.value().getDescription(gene)));
        return result;
    }

    public static GeneDescription unknown(Holder<Gene> gene) {
        final var result = new GeneDescription(gene);
        result.addChild(TextNode.description(UNKNOWN_DESCRIPTION));
        result.addChild(new ItemListDisplay(List.of(ENCHANTED_BOOK, ECHO_SHARD)));
        return result;
    }

    private GeneDescription(Holder<Gene> gene) {
        super(0, 5, 0);
        this.addChild(new GeneBigIcon(gene));
    }

    static {
        UNKNOWN_DESCRIPTION = Component.translatable("gene.archogenum.unknown.desc");
        ENCHANTED_BOOK = Items.ENCHANTED_BOOK.getDefaultInstance();
        ECHO_SHARD = Items.ECHO_SHARD.getDefaultInstance();
        final var echoGrasp = new EnchantmentInstance(ArchoEnchantments.ECHO_GRASP.get(), 1);
        EnchantedBookItem.addEnchantment(ENCHANTED_BOOK, echoGrasp);
    }
}
