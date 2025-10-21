package dev.obscuria.archogenum.world.genetics.basis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.archogenum.ArchogenumProxy;
import dev.obscuria.archogenum.registry.ArchoRegistries;
import dev.obscuria.fragmentum.network.PayloadCodec;
import dev.obscuria.fragmentum.registry.LazyHolder;
import dev.obscuria.fragmentum.registry.RegistryLazyCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record Archetype(
        @Unmodifiable List<MobCategory> mobCategories
) {

    public static final Codec<Holder<Archetype>> CODEC;
    public static final Codec<LazyHolder<Archetype>> LAZY_CODEC;
    public static final PayloadCodec<Holder<Archetype>> PAYLOAD_CODEC;
    public static final Codec<Archetype> DIRECT_CODEC;

    static {
        CODEC = RegistryFixedCodec.create(ArchoRegistries.Keys.ARCHETYPE);
        LAZY_CODEC = RegistryLazyCodec.create(ArchoRegistries.Keys.ARCHETYPE, ArchogenumProxy::registryAccess);
        PAYLOAD_CODEC = PayloadCodec.registryFriendly(CODEC, ArchogenumProxy::registryAccess);
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                MobCategory.CODEC.listOf().fieldOf("mob_categories").forGetter(Archetype::mobCategories)
        ).apply(codec, Archetype::new));
    }
}
