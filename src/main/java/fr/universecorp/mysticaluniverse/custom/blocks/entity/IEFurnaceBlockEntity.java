package fr.universecorp.mysticaluniverse.custom.blocks.entity;

import fr.universecorp.mysticaluniverse.custom.blocks.IEFurnaceBlock;
import fr.universecorp.mysticaluniverse.custom.recipe.EteriumIngotRecipe;
import fr.universecorp.mysticaluniverse.custom.screen.IEFurnaceScreenHandler;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IEFurnaceBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

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
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("iefurnace.progress");
        fuelTime = nbt.getInt("iefurnace.fueltime");
        maxFuelTime = nbt.getInt("iefurnace.maxfueltime");
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
        return new IEFurnaceScreenHandler(syncId, inv, this, this.propertyDelegate);
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
            if(hasFuelInSlot(entity) && !isConsumingFuel(entity)) {
                entity.consumeFuel();
            }

            if(isConsumingFuel(entity)) {
                entity.progress++;
                markDirty(world, blockPos, blockState);
            }

            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, blockState);
        }
    }

    private static boolean isConsumingFuel(IEFurnaceBlockEntity entity) {
        return entity.fuelTime > 0;
    }

    private static boolean hasFuelInSlot(IEFurnaceBlockEntity entity) {
        return !entity.getStack(0).isEmpty();
    }

    private static void craftItem(@NotNull IEFurnaceBlockEntity entity) {
        World world = entity.world;

        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i=0; i < inventory.size(); i++) {
            inventory.setStack(i, inventory.getStack(i));
        }

        if(hasRecipe(entity)) {
            entity.removeStack(1, 1);
            entity.setStack(2, new ItemStack(ModItems.ETERIUM_INGOT,
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
}