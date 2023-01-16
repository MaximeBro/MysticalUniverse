package fr.universecorp.mysticaluniverse.custom.networking;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEFurnaceBlockEntity;
import fr.universecorp.mysticaluniverse.custom.screen.IEFurnaceScreenHandler;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class FluidSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        FluidVariant fluidVariant = FluidVariant.fromPacket(buf);
        long fluidLevel = buf.readLong();
        BlockPos position = buf.readBlockPos();

        if(client.world.getBlockEntity(position) instanceof IEFurnaceBlockEntity blockEntity) {
            blockEntity.setFluidLevel(fluidVariant, fluidLevel);

            if(client.player.currentScreenHandler instanceof IEFurnaceScreenHandler screenHandler &&
                    screenHandler.blockEntity.getPos().equals(position)) {
                blockEntity.setFluidLevel(fluidVariant, fluidLevel);
                screenHandler.setFluid(new FluidStack(fluidVariant, fluidLevel));
            }
        }

    }


}
