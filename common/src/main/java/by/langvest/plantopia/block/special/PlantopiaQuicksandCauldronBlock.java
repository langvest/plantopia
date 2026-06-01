package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaCauldronInteraction;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
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

    public static boolean shouldHandlePrecipitation(Level level, BlockPos pos, Biome.Precipitation precipitation) {
        if (!NONE.test(precipitation)) return false;
        if (!level.getBiome(pos).is(PlantopiaBiomeTags.IS_DESERT)) return false;
        return level.getRandom().nextFloat() < 0.04F;
    }

    @Override
    public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
        if (isFull(state)) return;
        if (!shouldHandlePrecipitation(level, pos, precipitation)) return;

        var newState = state.cycle(LEVEL);
        level.setBlockAndUpdate(pos, newState);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide() && entity.isOnFire() && isEntityInsideContent(state, pos, entity)) {
            entity.clearFire();
        }
    }
}
