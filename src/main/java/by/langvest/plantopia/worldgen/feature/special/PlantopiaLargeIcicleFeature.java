package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.PlantopiaIcicleUtils;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaLargeIcicleConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class PlantopiaLargeIcicleFeature extends Feature<PlantopiaLargeIcicleConfiguration> {
    public PlantopiaLargeIcicleFeature(Codec<PlantopiaLargeIcicleConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaLargeIcicleConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        var config = context.config();
        var random = context.random();

        if (!PlantopiaIcicleUtils.isEmptyOrWater(level, origin)) {
            return false;
        }

        Optional<Column> optional = Column.scan(level, origin, config.floorToCeilingSearchRange(), PlantopiaIcicleUtils::isEmptyOrWater, PlantopiaIcicleUtils::isValidGround);
        if (optional.isEmpty() || !(optional.get() instanceof Column.Range range)) {
            return false;
        }

        if (range.height() < 4) {
            return false;
        }

        int i = (int) ((float) range.height() * config.maxColumnRadiusToCaveHeightRatio());
        int j = Mth.clamp(i, config.columnRadius().getMinValue(), config.columnRadius().getMaxValue());
        int k = Mth.randomBetweenInclusive(random, config.columnRadius().getMinValue(), j);

        var stalactite = makeIcicle(origin.atY(range.ceiling() - 1), false, random, k, config.stalactiteBluntness(), config.heightScale());
        var stalagmite = makeIcicle(origin.atY(range.floor() + 1), true, random, k, config.stalagmiteBluntness(), config.heightScale());

        WindOffsetter windOffsetter;
        if (stalactite.isSuitableForWind(config) && stalagmite.isSuitableForWind(config)) {
            windOffsetter = new WindOffsetter(origin.getY(), random, config.windSpeed());
        } else {
            windOffsetter = WindOffsetter.noWind();
        }

        boolean placeStalactite = stalactite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, windOffsetter);
        boolean placeStalagmite = stalagmite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, windOffsetter);

        if (placeStalactite) {
            stalactite.placeBlocks(level, random, windOffsetter, config);
        }

        if (placeStalagmite) {
            stalagmite.placeBlocks(level, random, windOffsetter, config);
        }

        return true;
    }


    @Contract("_, _, _, _, _, _ -> new")
    private static @NotNull LargeIcicle makeIcicle(BlockPos root, boolean pointingUp, RandomSource random, int radius, @NotNull FloatProvider bluntnessBase, @NotNull FloatProvider scaleBase) {
        return new LargeIcicle(root, pointingUp, radius, bluntnessBase.sample(random), scaleBase.sample(random));
    }

    private static final class LargeIcicle {
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;

        LargeIcicle(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
            this.root = root;
            this.pointingUp = pointingUp;
            this.radius = radius;
            this.bluntness = bluntness;
            this.scale = scale;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, WindOffsetter windOffsetter) {
            while (this.radius > 1) {
                var mutablePos = this.root.mutable();
                int searchDepth = Math.min(10, this.getHeight());

                for (int i = 0; i < searchDepth; ++i) {
                    if (PlantopiaIcicleUtils.isCircleMostlyEmbeddedInStone(level, windOffsetter.offset(mutablePos), this.radius)) {
                        this.root = mutablePos;
                        return true;
                    }

                    mutablePos.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }

                this.radius /= 2;
            }

            return false;
        }

        private int getHeightAtRadius(float radius) {
            return (int) PlantopiaIcicleUtils.getIcicleHeight(radius, this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(WorldGenLevel level, RandomSource random, WindOffsetter windOffsetter, PlantopiaLargeIcicleConfiguration config) {
            for (int x = -this.radius; x <= this.radius; ++x) {
                for (int z = -this.radius; z <= this.radius; ++z) {
                    float distance = Mth.sqrt((float) (x * x + z * z));
                    if (distance > (float) this.radius) {
                        continue;
                    }

                    int height = this.getHeightAtRadius(distance);
                    if (height <= 0) {
                        continue;
                    }

                    if (random.nextFloat() < 0.2D) {
                        height = (int) ((float) height * Mth.randomBetween(random, 0.8F, 1.0F));
                    }

                    var mutablePos = this.root.offset(x, 0, z).mutable();
                    int worldSurfaceY = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, mutablePos.getX(), mutablePos.getZ()) : Integer.MAX_VALUE;

                    for (int i = 0; i < height && mutablePos.getY() < worldSurfaceY; ++i) {
                        BlockPos pos = windOffsetter.offset(mutablePos);
                        if (PlantopiaIcicleUtils.isEmptyOrWater(level, pos)) {
                            BlockState blockState = config.provider().getState(random, pos);
                            level.setBlock(pos, blockState, 2);
                        }

                        mutablePos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                    }
                }
            }
        }

        boolean isSuitableForWind(@NotNull PlantopiaLargeIcicleConfiguration config) {
            return this.radius >= config.minRadiusForWind() && this.bluntness >= config.minBluntnessForWind();
        }
    }

    private static final class WindOffsetter {
        private final int originY;
        @Nullable
        private final Vec3 windSpeed;

        private WindOffsetter(int originY, RandomSource random, @NotNull FloatProvider magnitude) {
            this.originY = originY;
            float f = magnitude.sample(random);
            float f1 = Mth.randomBetween(random, 0.0F, (float) Math.PI);
            this.windSpeed = new Vec3(Mth.cos(f1) * f, 0.0D, Mth.sin(f1) * f);
        }

        private WindOffsetter() {
            this.originY = 0;
            this.windSpeed = null;
        }

        @Contract(value = " -> new", pure = true)
        static @NotNull WindOffsetter noWind() {
            return new WindOffsetter();
        }

        BlockPos offset(BlockPos pos) {
            if (this.windSpeed == null) {
                return pos;
            }

            int i = this.originY - pos.getY();
            var vec3 = this.windSpeed.scale(i);
            return pos.offset(Mth.floor(vec3.x), 0, Mth.floor(vec3.z));
        }
    }
}
