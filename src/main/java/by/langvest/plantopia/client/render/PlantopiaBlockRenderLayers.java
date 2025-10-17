package by.langvest.plantopia.client.render;

import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.property.PlantopiaRenderType;
import by.langvest.toolkit.event.client.RegisterRenderLayersEvent;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PlantopiaBlockRenderLayers {
	private static final Set<Block> CUTOUT = Sets.newHashSet();
	private static final Set<Block> CUTOUT_MIPPED = Sets.newHashSet();
	private static final Set<Block> TRANSLUCENT = Sets.newHashSet();

	public static void setup(RegisterRenderLayersEvent.@NotNull BlockEvent event) {
		generateAll();

		event.registerAll(CUTOUT, RenderType.cutout());
		event.registerAll(CUTOUT_MIPPED, RenderType.cutoutMipped());
		event.registerAll(TRANSLUCENT, RenderType.translucent());
	}

	private static void generateAll() {
		PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
			if(!blockMeta.shouldApplyRenderLayer()) return;

			var block = blockMeta.get();
			var renderType = blockMeta.getRenderType();

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
}