package fr.universecorp.mysticaluniverse.custom.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
    public static ScreenHandlerType<IEFurnaceScreenHandler> IEFURNACE_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        IEFURNACE_SCREEN_HANDLER = new ScreenHandlerType<>(IEFurnaceScreenHandler::new);
    }
}
