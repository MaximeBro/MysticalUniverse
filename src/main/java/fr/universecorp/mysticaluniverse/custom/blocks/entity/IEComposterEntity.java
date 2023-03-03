package fr.universecorp.mysticaluniverse.custom.blocks.entity;

import fr.universecorp.mysticaluniverse.custom.blocks.IEComposter;
import fr.universecorp.mysticaluniverse.networking.ModMessages;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
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

    private int infusingTime = 0;
    private int infusingMaxTime = 100;
    private boolean composting = false;
    private boolean infusing = false;
    private ItemStack renderStack = ItemStack.EMPTY;

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
                    case 2 -> IEComposterEntity.this.infusingTime;
                    case 3 -> IEComposterEntity.this.infusingMaxTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> IEComposterEntity.this.compostTime = value;
                    case 1 -> IEComposterEntity.this.maxCompostTime = value;
                    case 2 -> IEComposterEntity.this.infusingTime = value;
                    case 3 -> IEComposterEntity.this.infusingMaxTime = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.compostTime = nbt.getInt("iecomposter.compostTime");
        this.maxCompostTime = nbt.getInt("iecomposter.maxCompostTime");
        this.composting = nbt.getBoolean("iecomposter.isComposting");
        this.infusingTime = nbt.getInt("iecomposter.infusingTime");
        this.infusingMaxTime = nbt.getInt("iecomposter.infusingMaxTime");
        this.infusing = nbt.getBoolean("iecomposter.isInfusing");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("iecomposter.variant"));
        fluidStorage.amount = nbt.getLong("iecomposter.fluid");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("iecomposter.compostTime", this.compostTime);
        nbt.putInt("iecomposter.maxCompostTime", this.maxCompostTime);
        nbt.putBoolean("iecomposter.isComposting", this.composting);
        nbt.putInt("iecomposter.infusingTime", this.infusingTime);
        nbt.putInt("iecomposter.infusingMaxTime", this.infusingMaxTime);
        nbt.putBoolean("iecomposter.isInfusing", this.infusing);
        nbt.put("iecomposter.variant", fluidStorage.variant.toNbt());
        nbt.putLong("iecomposter.fluid", fluidStorage.amount);
    }


    public static void tick(World world, BlockPos blockPos, BlockState blockState, IEComposterEntity entity) {
        if(world.isClient) { return; }

        // Data Model (level of fluid rendered inside the IEComposter)
        long amount = entity.fluidStorage.getAmount();
        int level = 0;

        if(amount >= 100)  { level = 1; }
        if(amount >= 500)  { level = 2; }
        if(amount == 1000) { level = 3; }

        BlockState composter = world.getBlockState(blockPos);
        if(level != 0) {
            composter = composter.with(IEComposter.LEVEL, level).with(IEComposter.EMPTY, false);
            world.setBlockState(blockPos, composter);
        } else {
            composter = composter.with(IEComposter.EMPTY, true);
            world.setBlockState(blockPos, composter);
        }


        if(entity.isComposting()) {
            entity.compostTime--;
            if(entity.compostTime == 0) { addFluidFromComposting(entity); }
        }

        if(entity.isInfusing()) {
            entity.infusingTime--;
            if(entity.infusingTime == 0) {
                extractFluidWithAmount(entity, 500);
                Block.dropStack(world, new BlockPos(blockPos.getX(), blockPos.getY()+0.4f, blockPos.getZ()), new ItemStack(ModItems.INFUSED_LILY, 1));
            }
        }

        if(entity.compostTime  == 0) { entity.composting = false; }
        if(entity.infusingTime == 0) { entity.infusing   = false; }

        if(entity.fluidStorage.amount == 1000) { setRenderStack(entity, ModFluids.LIQUID_ETHER_BUCKET.getDefaultStack()); }
        else { setRenderStack(entity, ItemStack.EMPTY); }

        if(entity.isComposting()) { setRenderStack(entity, ModItems.BLUE_CLEMATITE_ESSENCE.getDefaultStack()); }
        if(entity.isInfusing()) { setRenderStack(entity, ModItems.ETHER_LILY.getDefaultStack()); }

        entity.sendFluidPacket();
        entity.sendRenderStackData();
        markDirty(world, blockPos, blockState);
    }

    @Override
    public void markDirty() {
        sendRenderStackData();
        super.markDirty();
    }

    public void startComposting() {
        this.composting = true;
        this.compostTime = this.maxCompostTime;
    }

    public void startInfusing() {
        this.infusing = true;
        this.infusingTime = this.infusingMaxTime;
    }

    public boolean isComposting() { return this.composting && this.compostTime > 0; }
    public boolean isInfusing() { return this.infusing && this.infusingTime > 0; }

    public void extractFluid() {
        extractFluidFromBucketing(this);
    }

    public void extractFluid(int amount) {
        extractFluidWithAmount(this, amount);
    }

    public static void extractFluidWithAmount(IEComposterEntity entity, int amount) {
        if(entity.fluidStorage.amount >= amount) {
            try(Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                        500, transaction);
                transaction.commit();
            }
        }
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

    public void sendRenderStackData() {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeItemStack(this.renderStack);
        buf.writeBlockPos(getPos());

        assert world != null;
        for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.COMPOSTER_RENDER_STACK, buf);
        }
    }

    public static void setRenderStack(IEComposterEntity entity, ItemStack stack) {
        entity.renderStack = stack;
    }

    public static ItemStack getRenderStack(IEComposterEntity entity) {
        return entity.renderStack;
    }


    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
    }
}
