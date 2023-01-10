package fr.universecorp.mysticaluniverse;

import fr.universecorp.mysticaluniverse.custom.screen.IEFurnaceScreen;
import fr.universecorp.mysticaluniverse.custom.screen.ModScreenHandlers;
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
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_LIQUID_ETHER, ModFluids.FLOWING_LIQUID_ETHER,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xA100C9FF
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_LIQUID_ETHER, ModFluids.FLOWING_LIQUID_ETHER);

        HandledScreens.register(ModScreenHandlers.IEFURNACE_SCREEN_HANDLER, IEFurnaceScreen::new);
    }
}