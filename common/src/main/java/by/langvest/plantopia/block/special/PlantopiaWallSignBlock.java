package by.langvest.plantopia.block.special;

import by.langvest.plantopia.blockentity.special.PlantopiaSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaWallSignBlock extends WallSignBlock {
    public PlantopiaWallSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlantopiaSignBlockEntity(pos, state);
    }
}
