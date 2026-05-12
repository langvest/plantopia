package by.langvest.plantopia.blockentity.special;

import by.langvest.plantopia.blockentity.PlantopiaBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlantopiaCobblestoneShardPetBlockEntity extends BlockEntity implements Nameable {
	protected Component name;

	public PlantopiaCobblestoneShardPetBlockEntity(BlockPos pos, BlockState state) {
		super(PlantopiaBlockEntityTypes.COBBLESTONE_SHARD_PET.get(), pos, state);
	}

	@Override
	public @NotNull Component getName() {
		if(name != null) return name;

		return Component.empty();
	}

	@Nullable
	public Component getCustomName() {
		return name;
	}

	public void setCustomName(Component name) {
		this.name = name;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		super.saveAdditional(tag);

		if(name != null) {
			tag.putString("CustomName", Component.Serializer.toJson(name));
		}
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		super.load(tag);

		if(tag.contains("CustomName", 8)) {
			name = Component.Serializer.fromJson(tag.getString("CustomName"));
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
