package by.langvest.toolkit.neoforge;

import by.langvest.toolkit.neoforge.client.NeoForgeRenderHelper;
import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.PlatformType;
import by.langvest.toolkit.platform.RegistryHelper;
import by.langvest.toolkit.platform.ResourceHelper;
import by.langvest.toolkit.platform.client.RenderHelper;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

public class NeoForgePlatform extends Platform {
    protected ResourceHelper resourceHelper;
    protected RegistryHelper registryHelper;
    protected RenderHelper renderHelper;

    public NeoForgePlatform(String modId) {
        super(modId);
        this.resourceHelper = new NeoForgeResourceHelper(this);
        this.registryHelper = new NeoForgeRegistryHelper(this);
        this.renderHelper = new NeoForgeRenderHelper(this);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.NEOFORGE;
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }

    @Override
    public boolean isServer() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    @Override
    public boolean isDatagen() {
        return DatagenModLoader.isRunningDataGen();
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
