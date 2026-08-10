package by.langvest.plantopia.block.special;

import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaStraightBalkStubBlock extends PlantopiaBalkStubBlock {
    public PlantopiaStraightBalkStubBlock(Properties properties, Supplier<Block> bulkBlock) {
        super(properties, bulkBlock);
    }
}
