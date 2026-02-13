package by.langvest.plantopia.client.render.item;

import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.util.helper.PlantopiaContentHelper;
import by.langvest.plantopia.util.helper.PlantopiaTickHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlantopiaItemStuckRenderer extends BlockEntityWithoutLevelRenderer {
	private final List<ItemLike> allFlowers = PlantopiaContentHelper.getAllFlowers();
	private final List<ItemLike> allHerbs = PlantopiaContentHelper.getAllHerbs();
	private final List<ItemLike> allMushrooms = PlantopiaContentHelper.getAllMushrooms();

	private static PlantopiaItemStuckRenderer instance;

	@SuppressWarnings("DataFlowIssue")
	public PlantopiaItemStuckRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
		super(dispatcher, modelSet);
	}

	public static PlantopiaItemStuckRenderer getInstance() {
		if(instance == null) {
			instance = new PlantopiaItemStuckRenderer(null, null);
		}

		return instance;
	}

	@Override
	public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if(itemStack.is(PlantopiaItems.FLOWERS_ICON.get())) {
			renderCollectionItem(allFlowers, displayContext, poseStack, buffer, packedLight, packedOverlay);
			return;
		}

		if(itemStack.is(PlantopiaItems.HERBS_ICON.get())) {
			renderCollectionItem(allHerbs, displayContext, poseStack, buffer, packedLight, packedOverlay);
			return;
		}

		if(itemStack.is(PlantopiaItems.MUSHROOMS_ICON.get())) {
			renderCollectionItem(allMushrooms, displayContext, poseStack, buffer, packedLight, packedOverlay);
		}
	}

	private void renderCollectionItem(@NotNull List<ItemLike> collection, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.translate(0.5F, 0.5f, 0.5f);
		int inGameTick = PlantopiaTickHelper.getInGameTick();
		int index = (inGameTick / 20) % collection.size();
		var instance = Minecraft.getInstance();
		var level = instance.level;
		var itemStack = collection.get(index).asItem().getDefaultInstance();
		instance.getItemRenderer().renderStatic(itemStack, displayContext, packedLight, packedOverlay, poseStack, buffer, level, 0);
	}
}