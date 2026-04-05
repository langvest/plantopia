package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.extension.PlantopiaAbstractCauldronBlockExtension;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractCauldronBlock.class)
public abstract class PlantopiaAbstractCauldronBlockMixin implements PlantopiaAbstractCauldronBlockExtension {
	@Shadow
	protected abstract boolean canReceiveStalactiteDrip(Fluid fluid);

	@Override
	public boolean plantopia$canReceiveStalactiteDrip(Fluid fluid) {
		return canReceiveStalactiteDrip(fluid);
	}
}
