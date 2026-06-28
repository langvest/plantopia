package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaCauldronInteraction;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaQuicksandCauldronBlock extends LayeredCauldronBlock {
    protected final Supplier<Block> contentBlock;

    public PlantopiaQuicksandCauldronBlock(Supplier<Block> contentBlock, Properties properties) {
        super(properties, RAIN, PlantopiaCauldronInteraction.QUICKSAND);
        this.contentBlock = contentBlock;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return Items.CAULDRON.getDefaultInstance();
    }

    public Block getContentBlock() {
        return contentBlock.get();
    }

    public static boolean shouldHandlePrecipitation(Level level, BlockPos pos, Biome.Precipitation precipitation) {
        return RAIN.test(precipitation) && level.getBiome(pos).is(PlantopiaBiomeTags.IS_QUICKSAND_PRECIPITABLE);
    }

    @Override
    public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
        if (level.getRandom().nextFloat() > 0.04F) return;

        BlockState newState = null;
        if (state.is(this)) {
            if (!isFull(state)) {
                newState = state.cycle(LEVEL);
            }
        } else {
            newState = defaultBlockState();
        }

        if (newState != null) {
            level.setBlockAndUpdate(pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide() && entity.isOnFire() && isEntityInsideContent(state, pos, entity)) {
            entity.clearFire();
        }
    }
}
