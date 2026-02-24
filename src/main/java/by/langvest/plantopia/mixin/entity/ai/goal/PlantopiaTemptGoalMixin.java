package by.langvest.plantopia.mixin.entity.ai.goal;

import by.langvest.plantopia.tag.PlantopiaItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptGoal.class)
public abstract class PlantopiaTemptGoalMixin {
	@Final
	@Shadow
	protected PathfinderMob mob;

	@Final
	@Shadow
	private Ingredient items;

	@Inject(
		method = "shouldFollow(Lnet/minecraft/world/entity/LivingEntity;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void shouldFollow(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		boolean isBee = mob instanceof Bee;
		if(!isBee) return;
		boolean shouldFollowMainHandItem = plantopia$shouldBeeFollowItem(entity.getMainHandItem());
		boolean shouldFollowOffhandItem = plantopia$shouldBeeFollowItem(entity.getOffhandItem());
		cir.setReturnValue(shouldFollowMainHandItem || shouldFollowOffhandItem);
	}

	@Unique
	private boolean plantopia$shouldBeeFollowItem(ItemStack itemStack) {
		return (items.test(itemStack) || itemStack.is(PlantopiaItemTags.PREFERRED_BY_BEES)) && !itemStack.is(PlantopiaItemTags.IGNORED_BY_BEES);
	}
}