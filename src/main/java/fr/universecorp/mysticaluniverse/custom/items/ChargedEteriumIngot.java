package fr.universecorp.mysticaluniverse.custom.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChargedEteriumIngot extends Item {

    public ChargedEteriumIngot(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("§7Press §e§oshift §7to see more informations"));

        if(Screen.hasShiftDown()) {
            tooltip.add(Text.literal("§b - Luminescent"));
            tooltip.add(Text.literal("§7Retains §9ether §7in it"));
        }
    }
}
