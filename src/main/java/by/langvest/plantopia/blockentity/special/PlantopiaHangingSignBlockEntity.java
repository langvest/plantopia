package by.langvest.plantopia.blockentity.special;

import by.langvest.plantopia.blockentity.PlantopiaBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHangingSignBlockEntity extends SignBlockEntity {
    public PlantopiaHangingSignBlockEntity(BlockPos pos, BlockState state) {
        super(PlantopiaBlockEntityTypes.HANGING_SIGN.get(), pos, state);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return PlantopiaBlockEntityTypes.HANGING_SIGN.get();
    }
}
