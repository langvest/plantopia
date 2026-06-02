package by.langvest.plantopia.mixin.level;

import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public abstract class PlantopiaServerLevelMixin {
    @Redirect(
        method = "tickIceAndSnow(ZLnet/minecraft/core/BlockPos;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"
        )
    )
    private Biome.Precipitation tickIceAndSnow$getPrecipitationAt(@NotNull Biome biome, BlockPos pos) {
        ServerLevel level = (ServerLevel) (Object) this;
        var originalPrecipitation = biome.getPrecipitationAt(pos);

        if (originalPrecipitation == Biome.Precipitation.NONE) {
            var biomeHolder = level.getBiome(pos.above());

            if (biomeHolder.is(PlantopiaBiomeTags.IS_QUICKSAND_PRECIPITABLE)) {
                return Biome.Precipitation.RAIN; // LanGvest: Imitate rain precipitation.
            }
        }

        return originalPrecipitation;
    }

    @Redirect(
        method = "tickIceAndSnow(ZLnet/minecraft/core/BlockPos;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
        )
    )
    private boolean tickChunk$shouldIce(@NotNull Biome biome, LevelReader levelReader, BlockPos pos) {
        ServerLevel level = (ServerLevel) (Object) this;

        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof PlantopiaFreezableBlock freezableBlock) {
            if (!biome.shouldFreeze(levelReader, pos)) return false;
            var iceState = Blocks.ICE.defaultBlockState();
            freezableBlock.freezeAt(state, iceState, level, pos, Block.UPDATE_ALL);
            return false;
        }

        return biome.shouldFreeze(levelReader, pos);
    }

    @Redirect(
        method = "tickIceAndSnow(ZLnet/minecraft/core/BlockPos;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
        )
    )
    private boolean tickChunk$shouldSnow(@NotNull Biome biome, LevelReader levelReader, BlockPos pos) {
        ServerLevel level = (ServerLevel) (Object) this;

        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof PlantopiaFreezableBlock || block instanceof SnowLayerBlock) {
            if (!biome.shouldSnow(levelReader, pos)) return false;

            int layers = block instanceof SnowLayerBlock ? state.getValue(SnowLayerBlock.LAYERS) : 0;
            int snowMaxHeight = level.getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);

            if (layers >= Math.min(snowMaxHeight, SnowLayerBlock.MAX_HEIGHT)) return false;

            if (block instanceof PlantopiaFreezableBlock freezableBlock) {
                var snowState = Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, layers + 1);

                freezableBlock.freezeAt(state, snowState, level, pos, Block.UPDATE_ALL);
                Block.pushEntitiesUp(state, level.getBlockState(pos), level, pos);

                return false;
            }

            if (block instanceof SnowLayerBlock) {
                var newState = state.setValue(SnowLayerBlock.LAYERS, layers + 1);

                level.setBlockAndUpdate(pos, newState);
                Block.pushEntitiesUp(state, newState, level, pos);

                return false;
            }
        }

        return biome.shouldSnow(levelReader, pos);
    }
}
