package by.langvest.plantopia.adv.trigger.special;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaBlockInteractTrigger extends SimpleCriterionTrigger<PlantopiaBlockInteractTrigger.TriggerInstance> {
	public static final ResourceLocation ID = plantopiaLocationFrom("block_interact");
	public static final PlantopiaBlockInteractTrigger INSTANCE = new PlantopiaBlockInteractTrigger();

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}

	@Override
	protected @NotNull PlantopiaBlockInteractTrigger.TriggerInstance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext context) {
		BlockPredicate block = BlockPredicate.fromJson(json.get("block"));
		return new TriggerInstance(playerPredicate, block);
	}

	public void trigger(@NotNull ServerPlayer player, BlockPos pos) {
		ServerLevel level = player.serverLevel();
		this.trigger(player, instance -> instance.matches(level, pos));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final BlockPredicate block;

		public TriggerInstance(ContextAwarePredicate playerPredicate, BlockPredicate block) {
			super(ID, playerPredicate);
			this.block = block;
		}

		public boolean matches(ServerLevel level, BlockPos pos) {
			return block.matches(level, pos);
		}

		@Override
		public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			json.add("block", block.serializeToJson());
			return json;
		}

		public static @NotNull TriggerInstance interactedWith(Block block) {
			BlockPredicate predicate = BlockPredicate.Builder.block().of(block).build();
			return new TriggerInstance(ContextAwarePredicate.ANY, predicate);
		}

		public static @NotNull TriggerInstance interactedWith(TagKey<Block> tag) {
			BlockPredicate predicate = BlockPredicate.Builder.block().of(tag).build();
			return new TriggerInstance(ContextAwarePredicate.ANY, predicate);
		}
	}
}
