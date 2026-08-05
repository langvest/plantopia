package by.langvest.plantopia.util;

import by.langvest.toolkit.util.LocationLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public final class PlantopiaDictionary {
    public static final String SINGLE = "single";
    public static final String PIT = "pit";
    public static final String LAKE = "lake";
    public static final String PATCH = "patch";
    public static final String STALACTITE = "stalactite";
    public static final String STALAGMITE = "stalagmite";
    public static final String BONEMEAL = "bonemeal";
    public static final String VEGETATION = "vegetation";
    public static final String COLONY = "colony";
    public static final String TREES = "trees";
    public static final String MOUNTAIN = "mountain";
    public static final String JUNGLE = "jungle";
    public static final String MARSH = "marsh";
    public static final String ROCK = "rock";
    public static final String ANCHOR = "anchor";
    public static final String CAVE = "cave";
    public static final String FLOWER = "flower";
    public static final String HUGE = "huge";
    public static final String SEASONAL = "seasonal";
    public static final String BOREAL = "boreal";
    public static final String RAIN = "rain";
    public static final String OLD_GROWTH = "old_growth";
    public static final String MAPLE = "maple";
    public static final String BIRCH = "birch";
    public static final String JACARANDA = "jacaranda";
    public static final String DEADWOOD = "deadwood";
    public static final String PALM = "palm";
    public static final String FIR = "fir";
    public static final String DEAD = "dead";
    public static final String LUPINE = "lupine";
    public static final String SPARSE = "sparse";
    public static final String GROVE = "grove";
    public static final String CLEARING = "clearing";
    public static final String GLADE = "glade";
    public static final String WOODS = "woods";
    public static final String THICKET = "thicket";
    public static final String FIELDS = "fields";
    public static final String VALE = "vale";
    public static final String LITTER = "litter";
    public static final String LUSH = "lush";
    public static final String BEES = "bees";
    public static final String TINY = "tiny";
    public static final String TALL = "tall";
    public static final String FANCY = "fancy";
    public static final String MEGA = "mega";
    public static final String CLUSTER = "cluster";
    public static final String SURFACE = "surface";
    public static final String LARGE = "large";
    public static final String WIDE = "wide";
    public static final String IN_WATER = "in_water";
    public static final String IN_SNOW = "in_snow";
    public static final String ON_SAND = "on_sand";
    public static final String RARE = "rare";
    public static final String BONUS = "bonus";
    public static final String SNOWY = "snowy";
    public static final String GRAVELLY = "gravelly";
    public static final String SANDY = "sandy";
    public static final String MUDDY = "muddy";
    public static final String SWAMP = "swamp";
    public static final String OCEAN = "ocean";
    public static final String CHECKED = "checked";
    public static final String MOSSY = "mossy";

    /* HELPER METHODS ******************************************/

    @Contract("_ -> new")
    public static @NotNull String singleNameOf(String name) {
        return compileNameFrom(SINGLE, name);
    }

    @Contract("_ -> new")
    public static @NotNull String patchNameOf(String name) {
        return compileNameFrom(PATCH, name);
    }

    @Contract("_ -> new")
    public static @NotNull String patchNameOf(Block block) {
        return compileNameFrom(PATCH, nameOf(block));
    }

    @Contract("_ -> new")
    public static @NotNull String singleNameOf(LocationLike locationLike) {
        return singleNameOf(nameOf(locationLike));
    }

    @Contract("_ -> new")
    public static @NotNull String patchNameOf(LocationLike locationLike) {
        return patchNameOf(nameOf(locationLike));
    }
}
