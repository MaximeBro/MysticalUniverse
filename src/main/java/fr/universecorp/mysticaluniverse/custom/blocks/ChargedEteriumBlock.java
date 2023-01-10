package fr.universecorp.mysticaluniverse.custom.blocks;


import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChargedEteriumBlock extends Block {

    public ChargedEteriumBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if(!Screen.hasShiftDown()) {
            tooltip.add(Text.literal("§b - Luminescent"));
            tooltip.add(Text.literal("§7Retains much §9ether §7in it"));
        }
    }
}
