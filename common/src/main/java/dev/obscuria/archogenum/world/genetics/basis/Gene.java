package dev.obscuria.archogenum.world.genetics.basis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.archogenum.world.genetics.trait.ITrait;
import dev.obscuria.fragmentum.network.PayloadCodec;
import dev.obscuria.fragmentum.registry.LazyHolder;
import dev.obscuria.fragmentum.util.color.RGB;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record Gene(
        Optional<LazyHolder<Archetype>> archetype,
        Optional<List<LazyHolder<ITrait>>> traits,
        GeneCategory category,
        ResourceLocation texture,
        String group,
        List<CostEntry> cost,
        boolean isArchogene,
        int maxExpression,
        int metabolicEfficiency,
        int complexity,
        RGB color
) {

    public static final Codec<Holder<Gene>> CODEC;
    public static final PayloadCodec<Holder<Gene>> PAYLOAD_CODEC;
    public static final Codec<Gene> DIRECT_CODEC;

    public MutableComponent getDisplayName(Holder<Gene> self) {
        final var id = self.unwrapKey().orElseThrow().location();
        return Component.translatable(id.toLanguageKey("gene"));
    }

    public MutableComponent getDescription(Holder<Gene> self) {
        final var id = self.unwrapKey().orElseThrow().location();
        return Component.translatable(id.toLanguageKey("gene", "desc"));
    }

    public void forEachTrait(Consumer<Holder<ITrait>> consumer) {
        if (traits.isEmpty()) return;
        for (var holder : traits.get()) {
            consumer.accept(holder.getOrThrow());
        }
    }

    public Stream<Holder<ITrait>> listTraits() {
        return traits.stream().flatMap(it -> it.stream().map(LazyHolder::getOrThrow));
    }

    public void appendTooltip(Holder<Gene> self, Consumer<Component> consumer) {
        consumer.accept(CommonComponents.EMPTY);
        consumer.accept(Component.literal("Complexity: " + complexity).withStyle(ChatFormatting.GOLD));
        consumer.accept(Component.literal("Metabolic Efficiency: " + metabolicEfficiency).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    public record CostEntry(
            Item item,
            int count
    ) {

        public static final Codec<CostEntry> CODEC;

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(CostEntry::item),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(CostEntry::count)
            ).apply(codec, CostEntry::new));
        }
    }

    static {
        CODEC = RegistryFixedCodec.create(ArchoRegistries.Keys.GENE);
        PAYLOAD_CODEC = PayloadCodec.registryFriendly(CODEC, ArchogenumProxy::registryAccess);
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Archetype.LAZY_CODEC.optionalFieldOf("archetype").forGetter(Gene::archetype),
                ITrait.LAZY_CODEC.listOf().optionalFieldOf("traits").forGetter(Gene::traits),
                GeneCategory.CODEC.fieldOf("category").forGetter(Gene::category),
                ResourceLocation.CODEC.fieldOf("texture").forGetter(Gene::texture),
                Codec.STRING.optionalFieldOf("group", ".generic").forGetter(Gene::group),
                CostEntry.CODEC.listOf().fieldOf("cost").forGetter(Gene::cost),
                Codec.BOOL.optionalFieldOf("is_archogene", false).forGetter(Gene::isArchogene),
                Codec.INT.fieldOf("max_expression").forGetter(Gene::maxExpression),
                Codec.INT.fieldOf("metabolic_efficiency").forGetter(Gene::metabolicEfficiency),
                Codec.INT.fieldOf("complexity").forGetter(Gene::complexity),
                RGB.CODEC.fieldOf("color").forGetter(Gene::color)
        ).apply(codec, Gene::new));
    }
}
