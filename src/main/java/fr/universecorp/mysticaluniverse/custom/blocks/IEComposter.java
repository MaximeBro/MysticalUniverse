package fr.universecorp.mysticaluniverse.custom.blocks;

import fr.universecorp.mysticaluniverse.client.screens.renderer.ColoredBlock;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.IEComposterEntity;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class IEComposter extends BlockWithEntity implements BlockEntityProvider, ColoredBlock {

    public static final IntProperty LEVEL = Properties.LEVEL_8;
    public static final BooleanProperty EMPTY = BooleanProperty.of("empty");
    public IEComposter(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(LEVEL, 0).with(EMPTY, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, EMPTY);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        IEComposterEntity entity = (IEComposterEntity) world.getBlockEntity(pos);

        // Right-Click with Blue Clematite's Essence
        if(player.getStackInHand(hand).getItem().equals(ModItems.BLUE_CLEMATITE_ESSENCE) && canCompostEssence(entity)) {
            if(!entity.isComposting()) {
                entity.startComposting();

                player.playSound(SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.5f, 1.0f);
                player.getStackInHand(hand).decrement(1);

                return ActionResult.SUCCESS;
            }
        }

        // Right-Click with empty bucket
        if(player.getStackInHand(hand).getItem().equals(Items.BUCKET) && entity.fluidStorage.amount == entity.fluidStorage.getCapacity()) {
            entity.extractFluid();

            player.playSound(SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            player.getStackInHand(hand).decrement(1);
            player.giveItemStack(new ItemStack(ModFluids.LIQUID_ETHER_BUCKET, 1));

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public boolean canCompostEssence(IEComposterEntity entity) {
        return entity.fluidStorage.amount + 250 <= entity.fluidStorage.getCapacity();
    }



    //******************
    // Block Entity PART
    //******************

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IEComposterEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.IECOMPOSTER, IEComposterEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public int getColor(int tint) {
        return 0;
    }

    @Override
    public int getColor(BlockState state, BlockPos pos, int tintIndex) {
        return 0;
    }
}
