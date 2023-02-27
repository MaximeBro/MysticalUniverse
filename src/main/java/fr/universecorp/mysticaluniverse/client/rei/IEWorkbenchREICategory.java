package fr.universecorp.mysticaluniverse.client.rei;

import fr.universecorp.mysticaluniverse.MysticalUniverse;
import fr.universecorp.mysticaluniverse.registry.ModBlocks;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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

public class IEWorkbenchREICategory implements DisplayCategory<IEWorkbenchREIDisplay> {

    private final EntryStack<ItemStack> IEWORKBENCH_ICON = EntryStacks.of(new ItemStack(ModBlocks.INFUSED_ETERIUM_WORKBENCH));

    @Override
    public CategoryIdentifier<? extends IEWorkbenchREIDisplay> getCategoryIdentifier() {
        return IEWorkbenchREIDisplay.ID;
    }

    @Override
    public Text getTitle() {
        return Text.of("IEWorkbench Crafting");
    }

    @Override
    public Renderer getIcon() {
        return this.IEWORKBENCH_ICON;
    }

    @Override
    public int getDisplayHeight() {
        return DisplayCategory.super.getDisplayHeight() + 55;
    }

    @Override
    public int getDisplayWidth(IEWorkbenchREIDisplay display) {
        return DisplayCategory.super.getDisplayWidth(display) + 50;
    }

    @Override
    public List<Widget> setupDisplay(IEWorkbenchREIDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();

        // Setup Background
        Point startPoint = new Point(bounds.getCenterX() - 20, bounds.getCenterY());
        widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight())));

        // Add input slots (x25)
        int x = startPoint.x - 45;
        int y = startPoint.y - 60;
        int offSetY = 0;
        int offSetX = 0;
        for(int i=0; i < 25; i++) {
            if(i % 5 == 0) { offSetY += 18; offSetX = 0; }
            widgets.add(createInputSlot(display, i, x + offSetX*18, y + offSetY));
            offSetX++;
        }

        // Add crafting arrow
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 50, startPoint.y - 5)));

        // Add fluid storage
        Identifier id = new Identifier(MysticalUniverse.MODID, "textures/gui/rei/ieworkbench_storage.png");
        Rectangle rec = new Rectangle(startPoint.x - 70, startPoint.y - 41, 16, 87);
        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) ->
                REIPlugin.DrawUtil.drawFluid(helper, matrices, id,0, 0, rec.x, rec.y, rec.width, rec.height, 15))));

        // Add fluid tooltip
        MutableText amountString = Text.translatable("mysticaluniverse.tooltip.liquid.amount",
                NumberFormat.getIntegerInstance().format(500));
        amountString.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
        widgets.add(Widgets.createTooltip(rec,
                Text.of(Text.translatable("block.mysticaluniverse.liquid_ether_block").getString()),
                amountString
        ));

        // Add recipe output name
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Text name = ((ItemStack) display.getOutputEntries().get(0).get(0).getValue()).getName();
        Label lbl = Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y - 55), name);
        widgets.add(lbl.noShadow().color(0xFF404040, 0xFFBBBBBB).centered());

        // Add output slot
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 85, startPoint.y - 5)));
        widgets.add(createOutputSlot(display, 0, startPoint.x + 85, startPoint.y - 5));


        return widgets;
    }
}
