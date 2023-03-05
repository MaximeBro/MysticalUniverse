package fr.universecorp.mysticaluniverse.client;

import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MysticalTab extends ItemGroup {

    private Text displayName;
    private Identifier id;
    private ItemStack stack;
    private BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay;

    @Override
    public ItemStack createIcon() {
        return this.stack;
    }

    @Override
    public String getTexture() {
        return "mystical_tab.png";
    }

    @Override
    public boolean hasScrollbar() {
        return true;
    }

    @Override
    public void appendStacks(DefaultedList<ItemStack> stacks) {
        if (stacksForDisplay != null) {
            stacksForDisplay.accept(stacks, this);
            return;
        }

        super.appendStacks(stacks);
    }

    private MysticalTab(Identifier id, BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay) {
        super(ItemGroup.GROUPS.length - 1, String.format("%s.%s", id.getNamespace(), id.getPath()));
        this.stacksForDisplay = stacksForDisplay;
    }

    public static class MysticalTabBuilder {
        private Text displayName;
        private Identifier id;
        private ItemStack stack = ItemStack.EMPTY;
        private Supplier<ItemStack> stackSupplier = () -> ItemStack.EMPTY;
        private BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay;

        public static MysticalTabBuilder create(Identifier id) {
            return new MysticalTabBuilder(id);
        }

        public MysticalTabBuilder(Identifier id) {
            this.id = id;
        }

        public MysticalTabBuilder key(String key) {
            this.displayName = Text.of(new Identifier(this.id.getNamespace(), key).toString());
            return this;
        }

        public MysticalTabBuilder icon(ItemStack stack) {
            this.stack = stack;
            return this;
        }

        public MysticalTabBuilder appendItems(Consumer<List<ItemStack>> stacksForDisplay) {
            return appendItems((itemStacks, itemGroup) -> stacksForDisplay.accept(itemStacks));
        }

        public MysticalTabBuilder appendItems(BiConsumer<List<ItemStack>, ItemGroup> stacksForDisplay) {
            this.stacksForDisplay = stacksForDisplay;
            return this;
        }

        public MysticalTab build() {
            ((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
            MysticalTab tab = new MysticalTab(this.id, this.stacksForDisplay);
            tab.displayName = this.displayName;
            tab.id = this.id;
            tab.stack = this.stack;
            return tab;
        }
    }
}
