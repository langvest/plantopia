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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlantopiaItemStuckRenderer extends BlockEntityWithoutLevelRenderer {
	private final List<ItemLike> allFlowers = PlantopiaContentHelper.getAllFlowers();
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
		int inGameTick = PlantopiaTickHelper.getInGameTick();
		Level level = Minecraft.getInstance().level;

		if(itemStack.is(PlantopiaItems.FLOWERS_ICON.get())) {
			poseStack.translate(0.5F, 0.5f, 0.5f);
			int index = (inGameTick / 20) % allFlowers.size();
			ItemStack flowerItem = allFlowers.get(index).asItem().getDefaultInstance();
			Minecraft.getInstance().getItemRenderer().renderStatic(flowerItem, displayContext, packedLight, packedOverlay, poseStack, buffer, level, 0);
		}
	}
}