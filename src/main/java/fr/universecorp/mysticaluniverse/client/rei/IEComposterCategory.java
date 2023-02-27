package fr.universecorp.mysticaluniverse.client.rei;

import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import fr.universecorp.mysticaluniverse.registry.ModFluids;
import fr.universecorp.mysticaluniverse.registry.ModItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Label;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.text.NumberFormat;
import java.util.List;

import static fr.universecorp.mysticaluniverse.client.rei.REIPlugin.createAnimatedArrow;
import static fr.universecorp.mysticaluniverse.client.rei.REIPlugin.createInputSlot;

public class IEComposterCategory implements DisplayCategory<IEComposterREIDisplay> {

    private final EntryStack<ItemStack> IECOMPOSTER_ICON = EntryStacks.of(new ItemStack(ModBlocks.INFUSED_ETERIUM_COMPOSTER));

    @Override
    public CategoryIdentifier<? extends IEComposterREIDisplay> getCategoryIdentifier() {
        return IEComposterREIDisplay.ID;
    }

    @Override
    public Text getTitle() {
        return Text.of("IEComposter Composting");
    }

    @Override
    public Renderer getIcon() {
        return this.IECOMPOSTER_ICON;
    }

    @Override
    public List<Widget> setupDisplay(IEComposterREIDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();

        // Setup Background
        Point startPoint = new Point(bounds.getCenterX(), bounds.getCenterY());
        widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight())));

        // Add input slot
        widgets.add(createInputSlot(display, 0, startPoint.x - 40, startPoint.y - 10));

        // Add progress arrow
        widgets.add(createAnimatedArrow(startPoint.x - 15, startPoint.y - 10));

        // Add fluid slot
        widgets.add(createInputSlot(display, 10, startPoint.x + 20, startPoint.y - 10));

        // Add fluid sprites texture
        Rectangle rec = new Rectangle(startPoint.x + 20, startPoint.y - 10, 16, 16);
        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) ->
                REIPlugin.DrawUtil.drawFluid(helper, matrices, rec.x, rec.y, rec.width, rec.height))));


        // Add fluid tooltip
        MutableText amountString = Text.translatable("mysticaluniverse.tooltip.liquid.amount",
                NumberFormat.getIntegerInstance().format(250));
        amountString.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
        widgets.add(Widgets.createTooltip(rec,
                Text.of(Text.translatable("block.mysticaluniverse.liquid_ether_block").getString()),
                amountString
        ));


        // Add recipe output name
        Text name = Text.of("Liquid Ether");
        Label lbl = Widgets.createLabel(new Point(startPoint.x, startPoint.y - 28), name);
        widgets.add(lbl.noShadow().color(0xFF404040, 0xFFBBBBBB));


        Text recipeInfo = Text.of("250 mB in 15 sec");
        Label lbl2 = Widgets.createLabel(new Point(startPoint.x, startPoint.y + 15), recipeInfo);
        widgets.add(lbl2.noShadow().color(0xFF404040, 0xFFBBBBBB));

        return widgets;
    }
}
