package fr.universecorp.mysticaluniverse.registry;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class ModFlammableBlocks {

    public static void registerFlammableBlocks() {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();

        registry.add(ModBlocks.MYCELIUM_LOG, 5, 5);
        registry.add(ModBlocks.MYCELIUM_PLANKS, 5, 5);
        registry.add(ModBlocks.MYCELIUM_LEAVES, 5, 5);
    }
}
