package by.langvest.plantopia.item;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;

public interface PlantopiaUpdateUseOnContext {
    UseOnContext updateUseOnContext(UseOnContext context, RandomSource syncRandom);
}
