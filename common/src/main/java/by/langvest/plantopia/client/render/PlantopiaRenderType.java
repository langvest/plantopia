package by.langvest.plantopia.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class PlantopiaRenderType extends RenderType {
    private static final RenderType CUTOUT_NO_CRUMBLING = create("cutout_no_crumbling", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 131072, false, false, RenderType.CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(RENDERTYPE_CUTOUT_SHADER).setTextureState(BLOCK_SHEET).createCompositeState(true));

    public PlantopiaRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType cutoutNoCrumbling() {
        return CUTOUT_NO_CRUMBLING;
    }
}
