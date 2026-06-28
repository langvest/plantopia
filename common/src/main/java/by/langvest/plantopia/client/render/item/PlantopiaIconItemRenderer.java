package by.langvest.plantopia.client.render.item;

import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.util.helper.PlantopiaContentHelper;
import by.langvest.plantopia.util.helper.PlantopiaTickHelper;
import by.langvest.toolkit.client.render.item.CustomItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlantopiaIconItemRenderer implements CustomItemRenderer {
    private final List<ItemLike> allFlowers = PlantopiaContentHelper.getAllFlowers();
    private final List<ItemLike> allHerbs = PlantopiaContentHelper.getAllHerbs();
    private final List<ItemLike> allMushrooms = PlantopiaContentHelper.getAllMushrooms();

    @Override
    public void renderByItem(RenderContext context) {
        var list = getDisplayList(context.itemStack());
        if (list != null && !list.isEmpty()) {
            renderList(list, context);
        }
    }

    @Nullable
    private List<ItemLike> getDisplayList(ItemStack itemStack) {
        if (itemStack.is(PlantopiaItems.FLOWERS_ICON.get())) return allFlowers;
        if (itemStack.is(PlantopiaItems.HERBS_ICON.get())) return allHerbs;
        if (itemStack.is(PlantopiaItems.MUSHROOMS_ICON.get())) return allMushrooms;
        return null;
    }

    private void renderList(List<ItemLike> list, RenderContext context) {
        int inGameTick = PlantopiaTickHelper.getInGameTick();
        int index = (inGameTick / 20) % list.size();
        var minecraft = Minecraft.getInstance();
        var itemStackToRender = list.get(index).asItem().getDefaultInstance();

        minecraft.getItemRenderer().renderStatic(
            itemStackToRender,
            context.displayContext(),
            context.combinedLight(),
            context.combinedOverlay(),
            context.poseStack(),
            context.buffer(),
            minecraft.level,
            0
        );
    }
}
