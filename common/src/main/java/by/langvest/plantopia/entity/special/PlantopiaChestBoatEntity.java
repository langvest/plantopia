package by.langvest.plantopia.entity.special;

import by.langvest.plantopia.entity.PlantopiaBoatLike;
import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.entity.PlantopiaEntityTypes;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaChestBoatEntity extends ChestBoat implements PlantopiaBoatLike {
    private static final EntityDataAccessor<Byte> DATA_ID_TYPE = SynchedEntityData.defineId(PlantopiaChestBoatEntity.class, EntityDataSerializers.BYTE);

    public PlantopiaChestBoatEntity(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public PlantopiaChestBoatEntity(Level level, double x, double y, double z) {
        super(PlantopiaEntityTypes.CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_ID_TYPE, (byte) 0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("ModType", PlantopiaRegistries.BOAT_TYPE.getKeyOrThrow(getBoatType()).toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("ModType", CompoundTag.TAG_STRING)) {
            var identifier = new ResourceLocation(compound.getString("ModType"));
            var type = PlantopiaRegistries.BOAT_TYPE.getValueOrThrow(identifier).get();
            this.setBoatType(type);
        }
    }

    @Override
    protected @NotNull Component getTypeName() {
        return EntityType.CHEST_BOAT.getDescription();
    }

    @Override
    public @NotNull Block getDropPlanks() {
        return getBoatType().planks().apply(this);
    }

    @Override
    public @NotNull Item getDropItem() {
        return getBoatType().item().apply(this);
    }

    /**
     * For compatibility purposes with the original class.
     */
    @Override
    public @NotNull Type getVariant() {
        return Type.OAK;
    }

    @Override
    public @NotNull PlantopiaBoatType getBoatType() {
        return PlantopiaBoatType.byId(entityData.get(DATA_ID_TYPE));
    }

    @Override
    public void setBoatType(PlantopiaBoatType type) {
        entityData.set(DATA_ID_TYPE, PlantopiaBoatType.toId(type));
    }
}
