package fr.universecorp.mysticaluniverse.custom.blocks.entity;

import fr.universecorp.mysticaluniverse.custom.networking.ModMessages;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IEComposterEntity extends BlockEntity {

    private int compostTime = 0;
    private int maxCompostTime = 300;
    private boolean composting = false;

    protected final PropertyDelegate propertyDelegate;


    public IEComposterEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IECOMPOSTER, pos, state);
        this.fluidStorage.variant = FluidVariant.of(ModFluids.STILL_LIQUID_ETHER);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> IEComposterEntity.this.compostTime;
                    case 1 -> IEComposterEntity.this.maxCompostTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> IEComposterEntity.this.compostTime = value;
                    case 1 -> IEComposterEntity.this.maxCompostTime = value;
                }
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.compostTime = nbt.getInt("iecomposter.compostTime");
        this.maxCompostTime = nbt.getInt("iecomposter.maxCompostTime");
        this.composting = nbt.getBoolean("iecomposter.isComposting");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("iecomposter.variant"));
        fluidStorage.amount = nbt.getLong("iecomposter.fluid");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("iecomposter.compostTime", this.compostTime);
        nbt.putInt("iecomposter.maxCompostTime", this.maxCompostTime);
        nbt.putBoolean("iecomposter.isComposting", this.composting);
        nbt.put("iecomposter.variant", fluidStorage.variant.toNbt());
        nbt.putLong("iecomposter.fluid", fluidStorage.amount);
    }


    public static void tick(World world, BlockPos blockPos, BlockState blockState, IEComposterEntity entity) {
        if(world.isClient) { return; }

        if(entity.isComposting()) {
            entity.compostTime--;
            if(entity.compostTime == 0) { addFluidFromComposting(entity); }
        }

        if(entity.compostTime == 0) { entity.composting = false; }

        entity.sendFluidPacket();
        markDirty(world, blockPos, blockState);
    }

    public static void markDirty(World world, BlockPos pos, BlockState state) {
        world.markDirty(pos);
        if (!state.isAir()) {
            world.updateComparators(pos, state.getBlock());
        }
    }

    public void startComposting() {
        this.composting = true;
        this.compostTime = this.maxCompostTime;
    }

    public boolean isComposting() { return this.composting; }
    public static int getCompostTime(IEComposterEntity entity) { return entity.propertyDelegate.get(0); }

    public void extractFluid() {
        extractFluidFromBucketing(this);
    }

    public static void addFluidFromComposting(IEComposterEntity entity) {
        if(entity.fluidStorage.amount < entity.fluidStorage.getCapacity() &&
                (entity.fluidStorage.amount + 250) <= entity.fluidStorage.getCapacity()) {

            try(Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.insert(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                        250, transaction);
                transaction.commit();
            }
        }
    }

    public static void extractFluidFromBucketing(IEComposterEntity entity) {
        if(entity.fluidStorage.amount == FluidStack.convertDropletsToMb(FluidConstants.BUCKET)) {
            try(Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                        1000, transaction);
                transaction.commit();
            }
        }
    }

    // ***************** //
    // FLUID HANDLING    //
    // ***************** //

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 1;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();

            if(!world.isClient()) {
                sendFluidPacket();
            }
        }
    };

    public void sendFluidPacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        fluidStorage.variant.toPacket(buf);
        buf.writeLong(fluidStorage.amount);
        buf.writeBlockPos(getPos());

        for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.FLUID_SYNC, buf);
        }
    }
    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
    }
}
