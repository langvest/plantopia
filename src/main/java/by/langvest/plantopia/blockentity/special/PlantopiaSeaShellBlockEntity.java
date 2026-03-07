package by.langvest.plantopia.blockentity.special;

import by.langvest.plantopia.blockentity.PlantopiaBlockEntities;
import by.langvest.plantopia.util.helper.PlantopiaColorHelper;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.hexToInt;

public class PlantopiaSeaShellBlockEntity extends BlockEntity {
	public static final int DEFAULT_COLOR = hexToInt("#83C2EA");
	protected int color = DEFAULT_COLOR;

	public PlantopiaSeaShellBlockEntity(BlockPos pos, BlockState state) {
		super(PlantopiaBlockEntities.SEA_SHELL.get(), pos, state);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		setChanged();
	}

	public static int generateRandomColor(@NotNull RandomSource random) {
		var h = PlantopiaMathHelper.getRandomFloatInclusive(random, 0.0F, 1.0F);
		var s = PlantopiaMathHelper.getRandomFloatInclusive(random, 0.22F, 0.58F);
		var b = PlantopiaMathHelper.getRandomFloatInclusive(random, 0.72F, 0.88F);

		return PlantopiaColorHelper.hsbToInt(h, s, b);
	}

	public void setRandomColor(@NotNull RandomSource random) {
		setColor(generateRandomColor(random));
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		super.saveAdditional(tag);

		tag.putInt("Color", color);
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		super.load(tag);

		if(tag.contains("Color")) {
			color = tag.getInt("Color");
		}
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}
}
