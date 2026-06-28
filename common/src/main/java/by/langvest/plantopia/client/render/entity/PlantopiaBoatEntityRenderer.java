package by.langvest.plantopia.client.render.entity;

import by.langvest.plantopia.entity.PlantopiaBoatLike;
import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

@ParametersAreNonnullByDefault
public class PlantopiaBoatEntityRenderer extends EntityRenderer<Boat> {
    protected final Map<PlantopiaBoatType, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public PlantopiaBoatEntityRenderer(EntityRendererProvider.Context context, boolean isChestBoat) {
        super(context);

        this.shadowRadius = 0.8F;
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

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if (!(boat instanceof PlantopiaBoatLike boatLike)) {
            throw new IllegalStateException("Tried to get boat type from a non-Plantopia boat!");
        }

        return boatResources.get(boatLike.getBoatType());
    }

    @Override
    public void render(Boat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        float f = (float) entity.getHurtTime() - partialTicks;
        float g = entity.getDamage() - partialTicks;
        if (g < 0.0F) {
            g = 0.0F;
        }

        if (f > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * g / 10.0F * (float) entity.getHurtDir()));
        }

        float h = entity.getBubbleAngle(partialTicks);
        if (!Mth.equal(h, 0.0F)) {
            poseStack.mulPose((new Quaternionf()).setAngleAxis(entity.getBubbleAngle(partialTicks) * ((float) Math.PI / 180F), 1.0F, 0.0F, 1.0F));
        }

        Pair<ResourceLocation, ListModel<Boat>> pair = getModelWithLocation(entity);
        ResourceLocation resourceLocation = pair.getFirst();
        ListModel<Boat> listModel = pair.getSecond();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        listModel.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = buffer.getBuffer(listModel.renderType(resourceLocation));
        listModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!entity.isUnderWater()) {
            VertexConsumer vertexConsumer2 = buffer.getBuffer(RenderType.waterMask());
            if (listModel instanceof WaterPatchModel waterPatchModel) {
                waterPatchModel.waterPatch().render(poseStack, vertexConsumer2, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Boat entity) {
        return getModelWithLocation(entity).getFirst();
    }
}
