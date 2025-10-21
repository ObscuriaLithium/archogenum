package dev.obscuria.archogenum.forge;

import dev.obscuria.archogenum.Archogenum;
import net.minecraftforge.fml.common.Mod;

@Mod(Archogenum.MODID)
public class ForgeArchogenum {

    public ForgeArchogenum() {
        Archogenum.init();
    }
}