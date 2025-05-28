package by.langvest.plantopia.client.render;

import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.property.PlantopiaRenderType;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class PlantopiaRenderTypes {
	private static final Set<Block> CUTOUT = Sets.newHashSet();
	private static final Set<Block> CUTOUT_MIPPED = Sets.newHashSet();
	private static final Set<Block> TRANSLUCENT = Sets.newHashSet();

	public static void setup() {
		registerAll();
		setAll();
	}

	@SuppressWarnings("unused")
	private static void add(@NotNull Set<Block> blockSet, Block... blocks) {
		blockSet.addAll(List.of(blocks));
	}

	private static void registerAll() {
		PlantopiaMetaRegistries.BLOCKS.forEach(blockMeta -> {
			if(!blockMeta.shouldApplyRenderLayer()) return;

			Block block = blockMeta.getBlock();
			PlantopiaRenderType renderType = blockMeta.getRenderType();

			if(renderType == PlantopiaRenderType.CUTOUT) {
				CUTOUT.add(block);
				return;
			}

			if(renderType == PlantopiaRenderType.CUTOUT_MIPPED) {
				CUTOUT_MIPPED.add(block);
				return;
			}

			if(renderType == PlantopiaRenderType.TRANSLUCENT) {
				TRANSLUCENT.add(block);
			}
		});
	}

	@SuppressWarnings("removal")
	private static void setAll() {
		RenderType cutout = RenderType.cutout();
		RenderType cutoutMipped = RenderType.cutoutMipped();
		RenderType translucent = RenderType.translucent();

		for(Block block : CUTOUT) ItemBlockRenderTypes.setRenderLayer(block, cutout);
		for(Block block : CUTOUT_MIPPED) ItemBlockRenderTypes.setRenderLayer(block, cutoutMipped);
		for(Block block : TRANSLUCENT) ItemBlockRenderTypes.setRenderLayer(block, translucent);
	}
}