package by.langvest.plantopia.blockentity.special;

import by.langvest.plantopia.blockentity.PlantopiaBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlantopiaSignBlockEntity extends SignBlockEntity {
    public PlantopiaSignBlockEntity(BlockPos pos, BlockState state) {
        super(PlantopiaBlockEntityTypes.SIGN.get(), pos, state);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return PlantopiaBlockEntityTypes.SIGN.get();
    }
}
