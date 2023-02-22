package fr.universecorp.mysticaluniverse.client.rei;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
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

import static fr.universecorp.mysticaluniverse.client.rei.REIPlugin.*;

public class IEFurnaceREICategory implements DisplayCategory<IEFurnaceREIDisplay> {

    private final EntryStack<ItemStack> IEFURNACE_ICON = EntryStacks.of(new ItemStack(ModBlocks.INFUSED_ETERIUM_FURNACE));

    @Override
    public CategoryIdentifier<? extends IEFurnaceREIDisplay> getCategoryIdentifier() {
        return IEFurnaceREIDisplay.ID;
    }

    @Override
    public Text getTitle() {
        return Text.literal("IEFurnace Infusing");
    }

    @Override
    public Renderer getIcon() {
        return this.IEFURNACE_ICON;
    }

    @Override
    public List<Widget> setupDisplay(IEFurnaceREIDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();

        // Setup Background
        Point startPoint = new Point(bounds.getCenterX(), bounds.getCenterY());
        widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight() + 20)));

        // Add input slot
        widgets.add(createInputSlot(display, 0, startPoint.x - 40, startPoint.y - 15));

        // Add fuel slot
        widgets.add(createInputSlot(display, 2, startPoint.x - 40, startPoint.y + 22));

        // Add progress arrow
        widgets.add(createAnimatedArrow(startPoint.x - 15, startPoint.y - 15));

        // Add burning fire
        widgets.add(createBurningFire(startPoint.x - 39, startPoint.y + 5));

        // Add fluid storage
        Identifier id = new Identifier(MysticalUniverse.MODID, "textures/gui/rei/iefurnace_storage.png");
        Rectangle rec = new Rectangle(startPoint.x - 65, startPoint.y - 18, 16, 60);
        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) ->
                REIPlugin.DrawUtil.drawFluid(helper, matrices, id,0, 0, rec.x, rec.y, rec.width, rec.height, 17))));

        // Add fluid tooltip
        MutableText amountString = Text.translatable("mysticaluniverse.tooltip.liquid.amount",
                                                           NumberFormat.getIntegerInstance().format(100));
        amountString.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
        widgets.add(Widgets.createTooltip(rec,
                Text.of(Text.translatable("block.mysticaluniverse.liquid_ether_block").getString()),
                amountString
        ));

        // Add recipe output name
        widgets.add(Widgets.createLabel(new Point(startPoint.x + 50, startPoint.y + 10),
                Text.of("")
        ));

        // Add output slot
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 20, startPoint.y - 15)));
        widgets.add(createOutputSlot(display, 0, startPoint.x + 20, startPoint.y - 15));

        return widgets;
    }
}
