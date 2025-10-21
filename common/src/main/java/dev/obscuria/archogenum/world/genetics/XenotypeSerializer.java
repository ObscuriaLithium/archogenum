package dev.obscuria.archogenum.world.genetics;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

public final class XenotypeSerializer implements EntityDataSerializer<Xenotype> {

    public static final XenotypeSerializer INSTANCE = new XenotypeSerializer();

    private XenotypeSerializer() {}

    @Override
    public void write(FriendlyByteBuf buf, Xenotype xenotype) {
        Xenotype.NETWORK_CODEC.write(buf, xenotype);
    }

    @Override
    public Xenotype read(FriendlyByteBuf buf) {
        return Xenotype.NETWORK_CODEC.read(buf);
    }

    @Override
    public Xenotype copy(Xenotype xenotype) {
        return xenotype.copy();
    }
}
