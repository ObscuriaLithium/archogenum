package dev.obscuria.archogenum.client.screen.elements.details;

import dev.obscuria.archogenum.client.screen.containers.GridContainer;
import dev.obscuria.archogenum.client.screen.elements.ItemDisplay;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.basis.Gene;
import dev.obscuria.archogenum.world.genetics.basis.IBundleLike;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class SynthesisCostDetails extends AbstractDetails {

    public static SynthesisCostDetails ofGene(Holder<Gene> gene) {
        return new SynthesisCostDetails(costOf(gene));
    }

    public static SynthesisCostDetails ofBundle(IBundleLike bundle) {
        return new SynthesisCostDetails(costOf(bundle));
    }

    public static SynthesisCostDetails ofXenotype(Xenotype xenotype) {
        final var stacks = new ArrayList<>(costOf(xenotype));
        stacks.add(0, xenotype.hasArchogene()
                ? Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance()
                : Items.GOLDEN_APPLE.getDefaultInstance());
        return new SynthesisCostDetails(stacks);
    }

    private SynthesisCostDetails(List<ItemStack> stacks) {
        super(Component.literal("Synthesis Cost"));
        if (stacks.isEmpty()) {
            this.rect.setVisible(false);
        } else {
            final var grid = new GridContainer(0);
            stacks.forEach(stack -> grid.addChild(new ItemDisplay(stack)));
            this.addChild(grid);
        }
    }

    private static List<ItemStack> costOf(Holder<Gene> gene) {
        return gene.value().cost().stream()
                .map(it -> new ItemStack(it.item(), it.count()))
                .toList();
    }

    private static List<ItemStack> costOf(IBundleLike bundle) {
        return bundle.genes().stream()
                .flatMap(it -> it.gene().value().cost().stream())
                .map(it -> new ItemStack(it.item(), it.count()))
                .toList();
    }
}
