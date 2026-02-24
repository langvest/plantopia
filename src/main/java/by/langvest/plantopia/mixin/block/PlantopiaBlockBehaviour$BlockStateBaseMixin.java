package by.langvest.plantopia.mixin.block;

import by.langvest.plantopia.block.PlantopiaOffsettableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class PlantopiaBlockBehaviour$BlockStateBaseMixin {
	@Mutable
	@Final
	@Shadow
	private Optional<BlockBehaviour.OffsetFunction> offsetFunction;

	@Redirect(
		method = "<init>(Lnet/minecraft/world/level/block/Block;Lcom/google/common/collect/ImmutableMap;Lcom/mojang/serialization/MapCodec;)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;offsetFunction:Ljava/util/Optional;",
			opcode = Opcodes.PUTFIELD
		)
	)
	private void setOffsetFunction(BlockBehaviour.BlockStateBase instance, Optional<BlockBehaviour.OffsetFunction> value, Block owner) {
		if(owner instanceof PlantopiaOffsettableBlock offsettableBlock) {
			this.offsetFunction = offsettableBlock.getOffsetFunction(value);
		} else {
			this.offsetFunction = value;
		}
	}
}