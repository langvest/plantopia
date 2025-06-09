package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaCauldronInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class PlantopiaQuicksandCauldronBlock extends LayeredCauldronBlock {
	public static final Predicate<Biome.Precipitation> NONE = (precipitation) -> precipitation == Biome.Precipitation.NONE;

	protected final Supplier<Block> contentBlock;

	public PlantopiaQuicksandCauldronBlock(Supplier<Block> contentBlock, Properties properties) {
		super(properties, NONE, PlantopiaCauldronInteraction.QUICKSAND);
		this.contentBlock = contentBlock;
	}

	public Block getContentBlock() {
		return contentBlock.get();
	}

	public static boolean shouldHandlePrecipitation(@NotNull Level level, @NotNull BlockPos pos, Biome.@NotNull Precipitation precipitation) {
		if(!NONE.test(precipitation)) return false;
		if(!level.getBiome(pos).is(Tags.Biomes.IS_DESERT)) return false;
		return level.getRandom().nextFloat() < 0.04F;
	}

	@Override
	public void handlePrecipitation(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Biome.@NotNull Precipitation precipitation) {
		if(isFull(state)) return;
		if(!shouldHandlePrecipitation(level, pos, precipitation)) return;

		var newState = state.cycle(LEVEL);
		level.setBlockAndUpdate(pos, newState);
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
	}
}
