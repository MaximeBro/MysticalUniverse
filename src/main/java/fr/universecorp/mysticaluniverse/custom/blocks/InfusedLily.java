package fr.universecorp.mysticaluniverse.custom.blocks;

import fr.universecorp.mysticaluniverse.custom.blocks.entity.CustomFlower;
import fr.universecorp.mysticaluniverse.custom.blocks.entity.InfusedLilyEntity;
import fr.universecorp.mysticaluniverse.custom.items.Billhook;
import fr.universecorp.mysticaluniverse.registry.ModBlockEntities;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InfusedLily extends CustomFlower implements BlockEntityProvider {

    public InfusedLily(StatusEffect suspiciousStewEffect, int effectDuration, AbstractBlock.Settings settings) {
        super(suspiciousStewEffect, effectDuration, settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        InfusedLilyEntity entity = (InfusedLilyEntity) world.getBlockEntity(pos);

        if(stack.getItem() instanceof Billhook && entity.hasEssence()) {
            stack.damage(1, player, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            entity.setEssence(false);
            Block.dropStack(world, new BlockPos(pos.getX(), pos.getY()+0.4f, pos.getZ()), new ItemStack(ModItems.BLUE_CLEMATITE_ESSENCE, 2));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }


    //******************
    // Block Entity PART
    //******************

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.INFUSED_LILY, InfusedLilyEntity::tick);
    }
}
