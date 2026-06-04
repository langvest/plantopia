package by.langvest.toolkit.fabric;

import by.langvest.toolkit.fabric.client.FabricRenderHelper;
import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.PlatformType;
import by.langvest.toolkit.platform.RegistryHelper;
import by.langvest.toolkit.platform.ResourceHelper;
import by.langvest.toolkit.platform.client.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform extends Platform {
    protected ResourceHelper resourceHelper;
    protected RegistryHelper registryHelper;
    protected RenderHelper renderHelper;

    public FabricPlatform(String modId) {
        super(modId);
        this.resourceHelper = new FabricResourceHelper(this);
        this.registryHelper = new FabricRegistryHelper(this);
        this.renderHelper = new FabricRenderHelper(this);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.FABRIC;
    }

    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isServer() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public boolean isDatagen() {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }

    @Override
    public RegistryHelper getRegistryHelper() {
        return registryHelper;
    }

    @Override
    public RenderHelper getRenderHelper() {
        return renderHelper;
    }
}
