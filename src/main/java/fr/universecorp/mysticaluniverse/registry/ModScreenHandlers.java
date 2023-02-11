package fr.universecorp.mysticaluniverse.registry;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.custom.screen.IEFurnaceScreenHandler;
import fr.universecorp.mysticaluniverse.custom.screen.IEWbRecipeBookScreenHandler;
import fr.universecorp.mysticaluniverse.custom.screen.IEWorkbenchScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlers {
    public static ScreenHandlerType<IEFurnaceScreenHandler> IEFURNACE_SCREEN_HANDLER;
    public static ScreenHandlerType<IEWorkbenchScreenHandler> IEWORKBENCH_SCREEN_HANDLER;

    public static ScreenHandlerType<IEWbRecipeBookScreenHandler> IEWB_RECIPEBOOK_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        IEFURNACE_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(IEFurnaceScreenHandler::new);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MysticalUniverse.MODID, "iefurnace"), IEFURNACE_SCREEN_HANDLER);

        IEWORKBENCH_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(IEWorkbenchScreenHandler::new);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MysticalUniverse.MODID, "ieworkbench"), IEWORKBENCH_SCREEN_HANDLER);

        IEWB_RECIPEBOOK_SCREEN_HANDLER = new ScreenHandlerType<>(IEWbRecipeBookScreenHandler::new);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MysticalUniverse.MODID, "ieworkbench_recipebook"), IEWB_RECIPEBOOK_SCREEN_HANDLER);
    }
}
