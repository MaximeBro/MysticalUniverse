package fr.universecorp.mysticaluniverse.custom.networking;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier FLUID_SYNC = new Identifier(MysticalUniverse.MODID, "fluid_sync");

    public static void registerS2CPacket() {
        ClientPlayNetworking.registerGlobalReceiver(FLUID_SYNC, FluidSyncS2CPacket::receive);
    }
}
