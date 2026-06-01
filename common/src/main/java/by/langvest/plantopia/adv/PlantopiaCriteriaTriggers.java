package by.langvest.plantopia.adv;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaCriteriaTriggers {
    public static final RegistryObject<ItemUsedOnLocationTrigger> BLOCK_INTERACT = registerTrigger("block_interact", ItemUsedOnLocationTrigger::new);

    public static <T extends CriterionTrigger<?>> RegistryObject<T> registerTrigger(String name, Supplier<T> supplier) {
        return registerTrigger(plantopia(name), supplier);
    }

    public static <T extends CriterionTrigger<?>> RegistryObject<T> registerTrigger(ResourceLocation identifier, Supplier<T> supplier) {
        return PlantopiaRegistries.TRIGGER_TYPE.register(identifier, supplier);
    }

    public static void setup(LifecycleEvent.CommonSetupEvent event) {
        PlantopiaRegistries.TRIGGER_TYPE.forEach(registryObject ->
            CriteriaTriggers.register(registryObject.getIdentifier().toString(), registryObject.get())
        );
    }

    public static @NotNull Criterion<ItemUsedOnLocationTrigger.TriggerInstance> interactedWith(Block block) {
        var contextAwarePredicate = ContextAwarePredicate.create(
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build()
        );

        return PlantopiaCriteriaTriggers.BLOCK_INTERACT.get()
            .createCriterion(new ItemUsedOnLocationTrigger.TriggerInstance(Optional.empty(), Optional.of(contextAwarePredicate)));
    }
}
