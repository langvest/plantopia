package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.PlantopiaIcicleUtils;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaIcicleColumnConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaIcicleColumnFeature extends Feature<PlantopiaIcicleColumnConfiguration> {
    public PlantopiaIcicleColumnFeature(Codec<PlantopiaIcicleColumnConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantopiaIcicleColumnConfiguration> context) {
        var config = context.config();
        var random = context.random();
        int height = config.height().sample(random);
        var direction = config.direction();
        var allowedPlacement = config.allowedPlacement();
        var originPos = context.origin();

        if (height <= 0) {
            return false;
        }

        var level = context.level();
        var baseState = PlantopiaIcicleUtils.getIcicleState(direction, DripstoneThickness.BASE);

        if (!mayPlaceAt(level, originPos, direction)) {
            return false;
        }

        if (!baseState.canSurvive(level, originPos)) {
            return false;
        }

        int finalHeight = 0;
        var mutablePos = originPos.mutable();
        for (int i = 0; i < height; i++) {
            var currentPos = mutablePos.move(direction, i);
            if (!PlantopiaNaturalBlockColumnFeature.isFavorablePos(baseState, level, currentPos, direction, allowedPlacement)) {
                break;
            }
            finalHeight++;
        }

        if (finalHeight <= 0) {
            return false;
        }

        return PlantopiaIcicleUtils.growIcicle(
            level,
            originPos,
            direction,
            finalHeight,
            Block.UPDATE_CLIENTS,
            false,
            random
        );
    }

    private boolean mayPlaceAt(WorldGenLevel level, BlockPos pos, Direction direction) {
        var attachedPos = pos.relative(direction.getOpposite());
        var attachedState = level.getBlockState(attachedPos);

        if (attachedState.is(PlantopiaIcicleUtils.getIcicleBlock())) {
            return false;
        }

        return attachedState.isFaceSturdy(level, attachedPos, direction);
    }

}
