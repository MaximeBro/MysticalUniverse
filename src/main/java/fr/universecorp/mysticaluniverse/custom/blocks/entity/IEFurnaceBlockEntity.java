package fr.universecorp.mysticaluniverse.custom.blocks.entity;

import fr.universecorp.mysticaluniverse.custom.blocks.IEFurnaceBlock;
import fr.universecorp.mysticaluniverse.custom.networking.ModMessages;
import fr.universecorp.mysticaluniverse.custom.recipe.EteriumIngotRecipe;
import fr.universecorp.mysticaluniverse.custom.screen.IEFurnaceScreenHandler;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IEFurnaceBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 2400;



    public IEFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IEFURNACE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {

            public int get(int index) {
                return switch (index) {
                    case 0 -> IEFurnaceBlockEntity.this.progress;
                    case 1 -> IEFurnaceBlockEntity.this.maxProgress;
                    case 2 -> IEFurnaceBlockEntity.this.fuelTime;
                    case 3 -> IEFurnaceBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }


            public void set(int index, int value) {
                switch (index) {
                    case 0 -> IEFurnaceBlockEntity.this.progress = value;
                    case 1 -> IEFurnaceBlockEntity.this.maxProgress = value;
                    case 2 -> IEFurnaceBlockEntity.this.fuelTime = value;
                    case 3 -> IEFurnaceBlockEntity.this.maxFuelTime = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }


    public static boolean canUseAsFuel(ItemStack stack) {
        if(stack.getItem() == ModItems.ETERIUM_COAL) {
            return true;
        }

        return false;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("iefurnace.progress", progress);
        nbt.putInt("iefurnace.fueltime", fuelTime);
        nbt.putInt("iefurnace.maxfueltime", maxFuelTime);
        nbt.put("iefurnace.variant", fluidStorage.variant.toNbt());
        nbt.putLong("iefurnace.fluid", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("iefurnace.progress");
        fuelTime = nbt.getInt("iefurnace.fueltime");
        maxFuelTime = nbt.getInt("iefurnace.maxfueltime");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("iefurnace.variant"));
        fluidStorage.amount = nbt.getLong("iefurnace.fluid");
    }

    private void consumeFuel() {
        if(!getStack(0).isEmpty()) {
            this.fuelTime = FuelRegistry.INSTANCE.get(this.removeStack(0, 1).getItem());
            this.maxFuelTime = this.fuelTime;
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Infused Eterium Furnace");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        sendFluidPacket();
        return new IEFurnaceScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, IEFurnaceBlockEntity entity) {
        if(world.isClient()) { return; }

        blockState = ((BlockState) blockState).with(IEFurnaceBlock.LIT, entity.fuelTime > 0);
        world.setBlockState(blockPos, blockState, 3);

        if(isConsumingFuel(entity)) {
            entity.fuelTime--;
        }

        if(hasRecipe(entity)) {
            if(hasFuelInSlot(entity) && !isConsumingFuel(entity) && hasEnoughFluid(entity)) {
                entity.consumeFuel();
            }

            if(isConsumingFuel(entity) && hasEnoughFluid(entity)) {
                entity.progress++;
                markDirty(world, blockPos, blockState);
            }

            if(entity.progress >= entity.maxProgress) {
                extractFluid(entity);
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, blockState);
        }

        if(hasFluidSourceInSlot(entity)) {
            transferFluidToFluidStorage(entity);
        }
    }

    private static void extractFluid(IEFurnaceBlockEntity entity) {
        try(Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                    100, transaction);
            transaction.commit();
        }
    }

    private static void transferFluidToFluidStorage(IEFurnaceBlockEntity entity) {
        if(entity.fluidStorage.amount < entity.fluidStorage.getCapacity() &&
          (entity.fluidStorage.amount + FluidStack.convertDropletsToMb(FluidConstants.BUCKET)) <= entity.fluidStorage.getCapacity()) {

            try(Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.insert(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                        FluidStack.convertDropletsToMb(FluidConstants.BUCKET), transaction);
                transaction.commit();

                entity.setStack(3, new ItemStack(Items.BUCKET));
            }
        }
    }

    private static boolean hasFluidSourceInSlot(IEFurnaceBlockEntity entity) {
        return entity.getStack(3).getItem() == ModFluids.LIQUID_ETHER_BUCKET;
    }

    private static boolean isConsumingFuel(IEFurnaceBlockEntity entity) {
        return entity.fuelTime > 0;
    }

    private static boolean hasFuelInSlot(IEFurnaceBlockEntity entity) {
        return !entity.getStack(0).isEmpty();
    }

    private static boolean hasEnoughFluid(IEFurnaceBlockEntity entity) {
        return entity.fluidStorage.amount >= 100;
    }

    private static void craftItem(@NotNull IEFurnaceBlockEntity entity) {
        World world = entity.world;

        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i=0; i < inventory.size(); i++) {
            inventory.setStack(i, inventory.getStack(i));
        }

        if(hasRecipe(entity)) {
            entity.removeStack(1, 1);
            entity.setStack(2, new ItemStack(ModItems.CHARGED_ETERIUM_INGOT,
                        entity.getStack(2).getCount() + 1));


            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(IEFurnaceBlockEntity entity) {
        World world = entity.world;

        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<EteriumIngotRecipe> match = world.getRecipeManager()
                .getFirstMatch(EteriumIngotRecipe.Type.INSTANCE, inventory, world);

        boolean macthPresent = match.isPresent();

        return macthPresent &&       canInsertAmountIntoOutputSlot(inventory) &&
                                     canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }


    public boolean isFuel(ItemStack stack) {
        return stack.getItem() == ModItems.ETERIUM_COAL;
    }



    // ***************** //
    // SIDED INVENTORIES
    // ***************** //
    /*
     * Front side : no insert/extract
     * Back side : insert item & fuel
     * Left side : insert item
     * Right side : insert item
     * Top side : insert item & fuel
     * Bottom side : extract item (from the output slot)
     */
    // ***************** //

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        Direction dir = this.getWorld().getBlockState(this.pos).get(IEFurnaceBlock.FACING);

        if(side == Direction.DOWN) { return false; }
        if(side == Direction.UP && slot == 1 && !isFuel(stack) || side == Direction.UP && slot == 0 && isFuel(stack)) { return true; }
        if(side == Direction.UP) { return false; }

        return switch(dir) {
            case SOUTH -> side == Direction.EAST && slot == 1 && !isFuel(stack)  || // RIGHT SIDE ITEM
                          side == Direction.WEST && slot == 1 && !isFuel(stack)  || // LEFT SIDE ITEM
                          side == Direction.NORTH && slot == 1 && !isFuel(stack) || // BACK SIDE ITEM
                          side == Direction.NORTH && slot == 0 && isFuel(stack);    // BACK SIDE FUEL

            case WEST -> side == Direction.SOUTH && slot == 1 && !isFuel(stack) || // RIGHT SIDE ITEM
                         side == Direction.NORTH && slot == 1 && !isFuel(stack) || // LEFT SIDE ITEM
                         side == Direction.EAST && slot == 1 && !isFuel(stack)  || // BACK SIDE ITEM
                         side == Direction.EAST && slot == 0 && isFuel(stack);     // BACK SIDE FUEL

            case EAST -> side == Direction.NORTH && slot == 1 && !isFuel(stack) || // RIGHT SIDE ITEM
                         side == Direction.SOUTH && slot == 1 && !isFuel(stack) || // LEFT SIDE ITEM
                         side == Direction.WEST && slot == 1 && !isFuel(stack)  || // BACK SIDE ITEM
                         side == Direction.WEST && slot == 0 && isFuel(stack);     // BACK SIDE FUEL

            case NORTH -> side == Direction.WEST && slot == 1 && !isFuel(stack)  || // RIGHT SIDE ITEM
                          side == Direction.EAST && slot == 1 && !isFuel(stack)  || // LEFT SIDE ITEM
                          side == Direction.SOUTH && slot == 1 && !isFuel(stack) || // BACK SIDE ITEM
                          side == Direction.SOUTH && slot == 0 && isFuel(stack);    // BACK SIDE FUEL

            default -> false;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && slot == 2;
    }


    // ***************** //
    // FLUID HANDLING
    // ***************** //

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10;
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