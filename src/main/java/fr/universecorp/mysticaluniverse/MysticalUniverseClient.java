package fr.universecorp.mysticaluniverse;

import fr.universecorp.mysticaluniverse.custom.networking.ModMessages;
import fr.universecorp.mysticaluniverse.custom.screen.IEFurnaceScreen;
import fr.universecorp.mysticaluniverse.custom.screen.IEWBRecipeBookScreen;
import fr.universecorp.mysticaluniverse.custom.screen.IEWorkbenchScreen;
import fr.universecorp.mysticaluniverse.registry.ModScreenHandlers;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MysticalUniverseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MYCELIUM_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MYCELIUM_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUE_CLEMATITE, RenderLayer.getCutout());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_LIQUID_ETHER, ModFluids.FLOWING_LIQUID_ETHER,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xA100C9FF
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_LIQUID_ETHER, ModFluids.FLOWING_LIQUID_ETHER);

        HandledScreens.register(ModScreenHandlers.IEFURNACE_SCREEN_HANDLER, IEFurnaceScreen::new);
        HandledScreens.register(ModScreenHandlers.IEWORKBENCH_SCREEN_HANDLER, IEWorkbenchScreen::new);
        HandledScreens.register(ModScreenHandlers.IEWB_RECIPEBOOK_SCREEN_HANDLER, IEWBRecipeBookScreen::new);

        ModMessages.registerS2CPacket();
    }
}
