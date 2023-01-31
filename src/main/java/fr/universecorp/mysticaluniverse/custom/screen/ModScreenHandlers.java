package fr.universecorp.mysticaluniverse.custom.screen;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlers {
    public static ScreenHandlerType<IEFurnaceScreenHandler> IEFURNACE_SCREEN_HANDLER;
    public static ScreenHandlerType<IEWorkbenchScreenHandler> IEWORKBENCH_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        IEFURNACE_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(IEFurnaceScreenHandler::new);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MysticalUniverse.MODID, "iefurnace"), IEFURNACE_SCREEN_HANDLER);

        IEWORKBENCH_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(IEWorkbenchScreenHandler::new);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MysticalUniverse.MODID, "ieworkbench"), IEWORKBENCH_SCREEN_HANDLER);
    }
}
