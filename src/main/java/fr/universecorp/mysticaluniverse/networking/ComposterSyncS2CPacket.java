package fr.universecorp.mysticaluniverse.networking;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ComposterSyncS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {

        World world = client.world;
        ItemStack stack = buf.readItemStack();
        BlockPos position = buf.readBlockPos();

        if(world != null) {
            if(world.getBlockEntity(position) instanceof IEComposterEntity entity) {
                IEComposterEntity.setRenderStack(entity, stack);
            }
        }
    }
}
