package by.langvest.plantopia.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationOf;

@Mixin(CarpetBlock.class)
public abstract class PlantopiaCarpetBlockMixin extends Block {
	@Unique
	private Optional<Boolean> plantopia$isMossCarpet = Optional.empty();

	public PlantopiaCarpetBlockMixin(@NotNull Properties properties) {
		super(properties.dynamicShape());
	}

	@Unique
	private boolean plantopia$isMossCarpet() {
		if(plantopia$isMossCarpet.isEmpty()) {
			plantopia$isMossCarpet = Optional.of(locationOf(this).equals(locationOf(Blocks.MOSS_CARPET)));
		}

		return plantopia$isMossCarpet.get();
	}

	@Unique
	private boolean plantopia$isFluidBelow(@NotNull BlockGetter level, @NotNull BlockPos pos) {
		var posBelow = pos.below();
		var fluidStateBelow = level.getFluidState(posBelow);

		return !fluidStateBelow.isEmpty();
	}

	@Unique
	private boolean plantopia$isFloatingMossCarpet(@NotNull BlockGetter level, @NotNull BlockPos pos) {
		return plantopia$isMossCarpet() && plantopia$isFluidBelow(level, pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		super.entityInside(state, level, pos, entity);

		if(level instanceof ServerLevel && entity instanceof Boat && plantopia$isFloatingMossCarpet(level, pos)) {
			level.destroyBlock(new BlockPos(pos), true, entity);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		if(plantopia$isMossCarpet()) {
			return Shapes.empty();
		}

		return super.getCollisionShape(state, level, pos, context);
	}
}
