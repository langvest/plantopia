package by.langvest.plantopia.block.special;

import by.langvest.plantopia.blockentity.special.PlantopiaHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaCeilingHangingSignBlock extends CeilingHangingSignBlock {
    public PlantopiaCeilingHangingSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlantopiaHangingSignBlockEntity(pos, state);
    }
}
