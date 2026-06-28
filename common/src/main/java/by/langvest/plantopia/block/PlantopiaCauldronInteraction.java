package by.langvest.plantopia.block;

import by.langvest.plantopia.item.PlantopiaItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.LayeredCauldronBlock;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public interface PlantopiaCauldronInteraction extends CauldronInteraction {
    Map<Item, CauldronInteraction> QUICKSAND = CauldronInteraction.newInteractionMap();

    PlantopiaCauldronInteraction FILL_QUICKSAND = (state, level, pos, player, hand, itemStack) -> CauldronInteraction.emptyBucket(
        level,
        pos,
        player,
        hand,
        itemStack,
        PlantopiaBlocks.QUICKSAND_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
        SoundEvents.BUCKET_EMPTY_POWDER_SNOW
    );

    static void setup() {
        QUICKSAND.put(Items.BUCKET, (state, level, pos, player, hand, itemStack) -> CauldronInteraction.fillBucket(
            state,
            level,
            pos,
            player,
            hand,
            itemStack,
            PlantopiaItems.QUICKSAND_BUCKET.get().getDefaultInstance(),
            (currentState) -> currentState.getValue(LayeredCauldronBlock.LEVEL) == 3,
            SoundEvents.BUCKET_FILL_POWDER_SNOW
        ));

        addDefaultInteractions(QUICKSAND, PlantopiaItems.QUICKSAND_BUCKET.get(), FILL_QUICKSAND);
    }

    static void addDefaultInteractions(Map<Item, CauldronInteraction> interactionMap, ItemLike item, CauldronInteraction interaction) {
        interactionMap.put(PlantopiaItems.QUICKSAND_BUCKET.get(), FILL_QUICKSAND);

        CauldronInteraction.addDefaultInteractions(interactionMap);
        expandVanillaDefaultInteractions(item, interaction);
    }

    static void expandVanillaDefaultInteractions(ItemLike item, CauldronInteraction interaction) {
        EMPTY.put(item.asItem(), interaction);
        WATER.put(item.asItem(), interaction);
        LAVA.put(item.asItem(), interaction);
        POWDER_SNOW.put(item.asItem(), interaction);
    }
}
