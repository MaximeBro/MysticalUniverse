package fr.universecorp.mysticaluniverse.custom.blocks;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEFurnaceBlockEntity;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class IEFurnaceBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.of("burning");

    public IEFurnaceBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }


    //******************
    // Block Entity PART
    //******************


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof IEFurnaceBlockEntity) {
                ItemScatterer.spawn(world, pos, (IEFurnaceBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        IEFurnaceBlockEntity entity = ((IEFurnaceBlockEntity) world.getBlockEntity(pos));

        // RIGHT CLICK WITH LIQUID ETHER BUCKET
        if(!player.getStackInHand(hand).isEmpty() && player.getStackInHand(hand).getItem() == ModFluids.LIQUID_ETHER_BUCKET) {

            if(entity.fluidStorage.amount < entity.fluidStorage.getCapacity() &&
              (entity.fluidStorage.amount + FluidStack.convertDropletsToMb(FluidConstants.BUCKET)) <= entity.fluidStorage.getCapacity()) {

                try(Transaction transaction = Transaction.openOuter()) {
                    entity.fluidStorage.insert(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                            FluidStack.convertDropletsToMb(FluidConstants.BUCKET), transaction);
                    transaction.commit();

                }

                player.setStackInHand(hand, new ItemStack(Items.BUCKET, 1));

            } else { openScreen(player, world, pos); }


        } else { // RIGHT CLICK WITH EMPTY BUCKET
            if(!player.getStackInHand(hand).isEmpty() && player.getStackInHand(hand).getItem() == Items.BUCKET) {

                if(entity.fluidStorage.amount >= FluidStack.convertDropletsToMb(FluidConstants.BUCKET)) {

                    try(Transaction transaction = Transaction.openOuter()) {
                        entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_LIQUID_ETHER),
                                FluidStack.convertDropletsToMb(FluidConstants.BUCKET), transaction);
                        transaction.commit();
                    }

                    player.getMainHandStack().decrement(1);
                    if(player.getMainHandStack().getCount() == 0) {
                        player.setStackInHand(hand, new ItemStack(ModFluids.LIQUID_ETHER_BUCKET, 1));
                    }
                    else { player.giveItemStack(new ItemStack(ModFluids.LIQUID_ETHER_BUCKET, 1)); }
                }


            } else { openScreen(player, world, pos); }
        }

        return ActionResult.SUCCESS;
    }

    public void openScreen(PlayerEntity player, World world, BlockPos pos) {
        if (!world.isClient()) {
            NamedScreenHandlerFactory screenHandlerFactory = ((IEFurnaceBlockEntity) world.getBlockEntity(pos));

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IEFurnaceBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.IEFURNACE, IEFurnaceBlockEntity::tick);
    }


    // Particle effects when the furnace is consuming fuel
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT)) {
            double d = (double)pos.getX() + 0.5;
            double e = (double)pos.getY();
            double f = (double)pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = (Direction)state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double h = random.nextDouble() * 0.6 - 0.3;
            double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52 : h;
            double j = random.nextDouble() * 6.0 / 16.0;
            double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52 : h;
            world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0, 0.0, 0.0);
            world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0, 0.0, 0.0);
        }
    }
}
