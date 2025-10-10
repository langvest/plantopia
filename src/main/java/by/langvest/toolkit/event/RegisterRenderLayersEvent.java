package by.langvest.toolkit.event;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class RegisterRenderLayersEvent<T, R> extends ClientEvent {
	protected final Registrar<T, R> registrar;

	public RegisterRenderLayersEvent(Registrar<T, R> registrar) {
		this.registrar = registrar;
	}

	public void register(T element, R renderType) {
		registrar.register(element, renderType);
	}

	public void registerAll(@NotNull Set<T> elements, R renderType) {
		elements.forEach(element -> registrar.register(element, renderType));
	}

	public static class BlockEvent extends RegisterRenderLayersEvent<Block, RenderType> {
		public BlockEvent(Registrar<Block, RenderType> registrar) {
			super(registrar);
		}
	}

	@FunctionalInterface
	public interface Registrar<T, R> {
		void register(T element, R renderType);
	}
}
