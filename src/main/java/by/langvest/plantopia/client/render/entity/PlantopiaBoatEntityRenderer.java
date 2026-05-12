package by.langvest.plantopia.client.render.entity;

import by.langvest.plantopia.entity.PlantopiaBoatLike;
import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

@ParametersAreNonnullByDefault
public class PlantopiaBoatEntityRenderer extends BoatRenderer {
    protected final Map<PlantopiaBoatType, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public PlantopiaBoatEntityRenderer(EntityRendererProvider.Context context, boolean isChestBoat) {
        super(context, isChestBoat);

        this.boatResources = PlantopiaRegistries.BOAT_TYPE.getAll()
            .stream()
            .collect(
                ImmutableMap.toImmutableMap(
                    Supplier::get,
                    registryObject -> {
                        var identifier = registryObject.getIdentifier();

                        return Pair.of(
                            textureLocation(identifier, isChestBoat),
                            createBoatModel(context, identifier, isChestBoat)
                        );
                    }
                )
            );
    }

    protected static @NotNull ResourceLocation textureLocation(ResourceLocation identifier, boolean isChestBoat) {
        return locationFrom(identifier.getNamespace(), "textures", "entity", isChestBoat ? "chest_boat" : "boat", identifier.getPath()).withSuffix(".png");
    }

    protected ListModel<Boat> createBoatModel(EntityRendererProvider.Context context, ResourceLocation identifier, boolean isChestBoat) {
        ModelLayerLocation modelLayerLocation = isChestBoat ? chestBoatModelLocation(identifier) : boatModelLocation(identifier);
        ModelPart modelPart = context.bakeLayer(modelLayerLocation);
        return isChestBoat ? new ChestBoatModel(modelPart) : new BoatModel(modelPart);
    }

    @Contract("_ -> new")
    public static @NotNull ModelLayerLocation boatModelLocation(ResourceLocation identifier) {
        return modelLocation(identifier.getNamespace(), "boat/" + identifier.getPath(), "main");
    }

    @Contract("_ -> new")
    public static @NotNull ModelLayerLocation chestBoatModelLocation(ResourceLocation identifier) {
        return modelLocation(identifier.getNamespace(), "chest_boat/" + identifier.getPath(), "main");
    }

    @Contract("_, _, _ -> new")
    public static @NotNull ModelLayerLocation modelLocation(String namespace, String path, String model) {
        return new ModelLayerLocation(locationFrom(namespace, path), model);
    }

    @Override
    public @NotNull Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if (boat instanceof PlantopiaBoatLike boatLike) {
            return boatResources.get(boatLike.getBoatType());
        }

        return super.getModelWithLocation(boat);
    }
}
