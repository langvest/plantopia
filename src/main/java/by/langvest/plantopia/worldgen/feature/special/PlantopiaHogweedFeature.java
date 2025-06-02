package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaHogweedBlock;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHogweedFeature extends Feature<NoneFeatureConfiguration> {
	public PlantopiaHogweedFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	/**
	 * Places the given feature at the given location.
	 * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
	 * that they can safely generate into.
	 * @param context A context object with a reference to the level and the position the feature is being placed at
	 */
	@Override
	public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
		PlantopiaHogweedBlock hogweedBlock = (PlantopiaHogweedBlock)PlantopiaBlocks.HOGWEED.get();
		var level = context.level();
		var pos = context.origin();

		if(hogweedBlock.canNaturallyPlaceAt(level, pos)) {
			PlantopiaHogweedBlock.placeAt(level, pos, hogweedBlock.defaultBlockState(), 19);
			return true;
		}

		return false;
	}
}
