package fr.universecorp.mysticaluniverse.custom.blocks.entity;

import fr.universecorp.mysticaluniverse.networking.ModMessages;
import fr.universecorp.mysticaluniverse.custom.recipe.IEWorkbenchCraftingInventory;
import fr.universecorp.mysticaluniverse.custom.recipe.IEWorkbenchShapedRecipes;
import fr.universecorp.mysticaluniverse.client.screens.IEWorkbenchScreenHandler;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IEWorkbenchBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private int bubbleTime = 0;
    private int bubbleMaxTime = 16;
    private int isCraftAvailable = 0;
    protected final PropertyDelegate propertyDelegate;
    private static IEWorkbenchScreenHandler screenHandler;

    public IEWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IEWORKBENCH, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> IEWorkbenchBlockEntity.this.bubbleTime;
                    case 1 -> IEWorkbenchBlockEntity.this.bubbleMaxTime;
                    case 2 -> IEWorkbenchBlockEntity.this.isCraftAvailable;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                  case 0 -> IEWorkbenchBlockEntity.this.bubbleTime = value;
                  case 1 -> IEWorkbenchBlockEntity.this.bubbleMaxTime = value;
                  case 2 -> IEWorkbenchBlockEntity.this.isCraftAvailable = value;
                };
            }

            @Override
            public int size() {
                return 3;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        sendFluidPacket();
        screenHandler = new IEWorkbenchScreenHandler(syncId, inv, this, this.propertyDelegate);
        return screenHandler;
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.put("ieworkbench.variant", fluidStorage.variant.toNbt());
        nbt.putLong("ieworkbench.fluid", fluidStorage.amount);
        nbt.putInt("ieworkbench.bubbleTime", bubbleTime);
        nbt.putInt("ieworkbench.bubbleMaxTime", bubbleMaxTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("ieworkbench.variant"));
        fluidStorage.amount = nbt.getLong("ieworkbench.fluid");
        this.bubbleTime = nbt.getInt("ieworkbench.bubbleTime");
        this.bubbleMaxTime = nbt.getInt("ieworkbench.bubbleMaxTime");
    }


    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, IEWorkbenchBlockEntity entity) {
        if(world.isClient()) { return; }

        if(isConsumingEssence(entity)) {
            entity.bubbleTime--;
        }

        if(hasEssenceInSlot(entity) && !isConsumingEssence(entity)) {
            consumeEssence(entity);
        }

        if(hasRecipe(entity) && hasEnoughFluid(entity)) { entity.propertyDelegate.set(2, 1); craftItem(entity); }
        else { entity.propertyDelegate.set(2, 0); entity.setStack(0, ItemStack.EMPTY); }

        markDirty(world, blockPos, blockState);
    }

    public static boolean hasRecipe(IEWorkbenchBlockEntity entity) {
        World world = entity.world;

        if(screenHandler != null) {

            IEWorkbenchCraftingInventory inventory = new IEWorkbenchCraftingInventory(screenHandler, 5, 5);
            for (int i = 2; i < entity.inventory.size(); i++) {
                inventory.setStack(i - 2, entity.getStack(i));
            }

            Optional<IEWorkbenchShapedRecipes> recipe = world.getRecipeManager().getFirstMatch(
                    IEWorkbenchShapedRecipes.Type.INSTANCE, inventory, world);

            boolean isPresent = recipe.isPresent();

            return isPresent;
        }
        return false;
    }

    public static boolean isConsumingEssence(IEWorkbenchBlockEntity entity) {
        return entity.bubbleTime > 0;
    }

    public static boolean hasEssenceInSlot(IEWorkbenchBlockEntity entity) {
        return !entity.inventory.get(1).isEmpty() && entity.inventory.get(1).getItem().equals(ModItems.BLUE_CLEMATITE_ESSENCE);
    }

    public static void consumeEssence(IEWorkbenchBlockEntity entity) {
        transferFluidToFluidStorage(entity);
        entity.bubbleMaxTime = entity.bubbleTime = 16;
    }


    public static void craftItem(IEWorkbenchBlockEntity entity) {
        World world = entity.world;

        IEWorkbenchCraftingInventory inventory = new IEWorkbenchCraftingInventory(screenHandler, 5, 5);
        for (int i = 2; i < entity.inventory.size(); i++) {
            inventory.setStack(i-2, entity.getStack(i));
        }

        Optional<IEWorkbenchShapedRecipes> recipe = world.getRecipeManager().getFirstMatch(
                IEWorkbenchShapedRecipes.Type.INSTANCE, inventory, world);

        if(hasRecipe(entity)) {
            if(entity.getStack(0).isEmpty()) {
                entity.setStack(0, new ItemStack(recipe.get().getOutput().getItem(), 1));
            }
        }
    }


    // ***************** //
    // FLUID HANDLING    //
    // ***************** //


    public static void extractFluid(IEWorkbenchBlockEntity entity) {
        try(Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                    500, transaction);
            transaction.commit();
        }
    }

    private static boolean hasEnoughFluid(IEWorkbenchBlockEntity entity) {
        return entity.fluidStorage.amount >= 500;
    }

    private static void transferFluidToFluidStorage(IEWorkbenchBlockEntity entity) {
        if(entity.fluidStorage.amount < entity.fluidStorage.getCapacity() &&
                (entity.fluidStorage.amount + 50) <= entity.fluidStorage.getCapacity()) {

            try(Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.insert(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                        50, transaction);
                transaction.commit();
                entity.getStack(1).decrement(1);
            }
        }
    }

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 20;
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




    // ***************** //
    // SIDED INVENTORIES //
    // ***************** //


    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public Text getDisplayName() {
        return Text.of("IEWorkbench");
    }
}
