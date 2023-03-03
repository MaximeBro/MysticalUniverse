package fr.universecorp.mysticaluniverse.networking;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEFurnaceBlockEntity;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEWorkbenchBlockEntity;
import fr.universecorp.mysticaluniverse.client.screens.IEFurnaceScreenHandler;
import fr.universecorp.mysticaluniverse.client.screens.IEWorkbenchScreenHandler;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        FluidVariant fluidVariant = FluidVariant.fromPacket(buf);
        long fluidLevel = buf.readLong();
        BlockPos position = buf.readBlockPos();

        World world = client.world;

        if(world != null) {
            if(client.world.getBlockEntity(position) instanceof IEFurnaceBlockEntity blockEntity) {
                blockEntity.setFluidLevel(fluidVariant, fluidLevel);

                if(client.player.currentScreenHandler instanceof IEFurnaceScreenHandler screenHandler &&
                        screenHandler.blockEntity.getPos().equals(position)) {
                    blockEntity.setFluidLevel(fluidVariant, fluidLevel);
                    screenHandler.setFluid(new FluidStack(fluidVariant, fluidLevel));
                }
            }

            if(client.world.getBlockEntity(position) instanceof IEWorkbenchBlockEntity blockEntity) {
                blockEntity.setFluidLevel(fluidVariant, fluidLevel);

                if(client.player.currentScreenHandler instanceof IEWorkbenchScreenHandler screenHandler &&
                        screenHandler.blockEntity.getPos().equals(position)) {
                    blockEntity.setFluidLevel(fluidVariant, fluidLevel);
                    screenHandler.setFluid(new FluidStack(fluidVariant, fluidLevel));
                }
            }

            if(client.world.getBlockEntity(position) instanceof IEComposterEntity blockentity) {
                blockentity.setFluidLevel(fluidVariant, fluidLevel);
            }
        }
    }


}
