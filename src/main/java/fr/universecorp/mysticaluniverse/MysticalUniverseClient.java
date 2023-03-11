package fr.universecorp.mysticaluniverse;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import fr.universecorp.mysticaluniverse.client.renderers.IEComposterFluidRenderer;
import fr.universecorp.mysticaluniverse.entity.DruidEntityModel;
import fr.universecorp.mysticaluniverse.entity.renderers.DruidEntityRenderer;
import fr.universecorp.mysticaluniverse.entity.ModEntities;
import fr.universecorp.mysticaluniverse.registry.ModMessages;
import fr.universecorp.mysticaluniverse.client.screens.IEFurnaceScreen;
import fr.universecorp.mysticaluniverse.client.screens.IEWbRecipeBookScreen;
import fr.universecorp.mysticaluniverse.client.screens.IEWorkbenchScreen;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModScreenHandlers;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MysticalUniverseClient implements ClientModInitializer {

    public static final EntityModelLayer DRUID_LAYER = new EntityModelLayer(new Identifier(MysticalUniverse.MODID, "druid"), "main");
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MYCELIUM_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MYCELIUM_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUE_CLEMATITE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ETHER_LILY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.INFUSED_LILY, RenderLayer.getCutout());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_LIQUID_ETHER, ModFluids.FLOWING_LIQUID_ETHER,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xA100C9FF
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_LIQUID_ETHER, ModFluids.FLOWING_LIQUID_ETHER);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xA100C9FF, ModBlocks.INFUSED_ETERIUM_COMPOSTER);

        ModMessages.registerS2CPacket();
        BlockEntityRendererRegistry.register(ModBlockEntities.IECOMPOSTER, IEComposterFluidRenderer::new);
        EntityRendererRegistry.register(ModEntities.DRUID_ENTITY, DruidEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(DRUID_LAYER, DruidEntityModel::getTextureModelData);

        HandledScreens.register(ModScreenHandlers.IEFURNACE_SCREEN_HANDLER, IEFurnaceScreen::new);
        HandledScreens.register(ModScreenHandlers.IEWORKBENCH_SCREEN_HANDLER, IEWorkbenchScreen::new);
        HandledScreens.register(ModScreenHandlers.IEWB_RECIPEBOOK_SCREEN_HANDLER, IEWbRecipeBookScreen::new);
    }
}
