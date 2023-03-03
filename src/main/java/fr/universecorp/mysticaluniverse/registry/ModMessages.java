package fr.universecorp.mysticaluniverse.registry;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.networking.ComposterSyncS2CPacket;
import fr.universecorp.mysticaluniverse.networking.FluidSyncS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier FLUID_SYNC = new Identifier(MysticalUniverse.MODID, "fluid_sync");
    public static final Identifier COMPOSTER_RENDER_STACK = new Identifier(MysticalUniverse.MODID, "composter_render_stack");

    public static void registerS2CPacket() {
        ClientPlayNetworking.registerGlobalReceiver(FLUID_SYNC, FluidSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(COMPOSTER_RENDER_STACK, ComposterSyncS2CPacket::receive);
    }
}
