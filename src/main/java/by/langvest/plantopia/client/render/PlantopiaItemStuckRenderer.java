package by.langvest.plantopia.client.render;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PlantopiaItemStuckRenderer extends BlockEntityWithoutLevelRenderer {
	private static final List<Block> ALL_FLOWERS = PlantopiaContentHelper.getAllFlowers();
	private static PlantopiaItemStuckRenderer instance = null;

	@SuppressWarnings("DataFlowIssue")
	public PlantopiaItemStuckRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
		super(dispatcher, modelSet);
	}

	public static PlantopiaItemStuckRenderer getInstance() {
		if(instance == null) instance = new PlantopiaItemStuckRenderer(null, null);
		return instance;
	}

	@Override
	public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
		int inGameTick = PlantopiaTickHelper.getInGameTick();
		Level level = Minecraft.getInstance().level;

		if(itemStack.is(PlantopiaItems.FLOWERS_ICON.get())) {
			poseStack.translate(0.5F, 0.5f, 0.5f);
			int index = (inGameTick / 20) % ALL_FLOWERS.size();
			ItemStack flowerItem = new ItemStack(ALL_FLOWERS.get(index));
			Minecraft.getInstance().getItemRenderer().renderStatic(flowerItem, displayContext, packedLight, packedOverlay, poseStack, buffer, level, 0);
		}
	}
}