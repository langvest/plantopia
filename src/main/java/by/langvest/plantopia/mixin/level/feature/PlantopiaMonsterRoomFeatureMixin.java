package by.langvest.plantopia.mixin.level.feature;

import by.langvest.plantopia.worldgen.feature.PlantopiaMiscOverworldFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MonsterRoomFeature.class)
public abstract class PlantopiaMonsterRoomFeatureMixin {
    @Inject(
        method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
        at = @At("RETURN")
    )
    public void place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, @NotNull CallbackInfoReturnable<Boolean> cir) {
        boolean isMonsterRoomPlacedSuccessfully = cir.getReturnValue();
        var level = context.level();
        var originPos = context.origin();
        var random = context.random();
        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, originPos.getX(), originPos.getZ());
        int depth = surfaceY - originPos.getY();

        float probability = 0.0F;

        if (originPos.getY() < surfaceY) {
            if (isMonsterRoomPlacedSuccessfully) {
                float delta = Mth.clamp(depth, 0, 64) / 64.0F;
                probability = Mth.lerp(delta, 0.82F, 0.12F);
            } else {
                probability = 0.00018F;
            }
        }

        if (random.nextFloat() < probability) {
            var chunkGenerator = context.chunkGenerator();
            var anchorFeature = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(PlantopiaMiscOverworldFeatures.FAIRY_RING_ANCHOR);
            anchorFeature.ifPresent(feature -> feature.value().place(level, chunkGenerator, random, originPos));
        }
    }
}
