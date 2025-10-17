package by.langvest.toolkit.event.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class RegisterRenderLayersEvent extends ClientEvent {
	public static abstract class BlockEvent extends RegisterRenderLayersEvent {
		public abstract void register(Block block, RenderType renderType);

		public void registerAll(@NotNull Set<Block> blocks, RenderType renderType) {
			for(var block : blocks) {
				register(block, renderType);
			}
		}
	}
}
