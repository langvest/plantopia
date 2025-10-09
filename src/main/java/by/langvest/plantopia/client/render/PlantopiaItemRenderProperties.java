package by.langvest.plantopia.client.render;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

@OnlyIn(Dist.CLIENT)
public class PlantopiaItemRenderProperties implements IClientItemExtensions {
	private static PlantopiaItemRenderProperties instance = null;

	private PlantopiaItemRenderProperties() {}

	public static PlantopiaItemRenderProperties getInstance() {
		if(instance == null) {
			instance = new PlantopiaItemRenderProperties();
		}

		return instance;
	}

	@Override
	public BlockEntityWithoutLevelRenderer getCustomRenderer() {
		return PlantopiaItemStuckRenderer.getInstance();
	}
}
