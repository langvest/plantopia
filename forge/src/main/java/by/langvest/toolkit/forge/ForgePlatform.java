package by.langvest.toolkit.forge;

import by.langvest.toolkit.forge.client.ForgeRenderHelper;
import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.PlatformType;
import by.langvest.toolkit.platform.RegistryHelper;
import by.langvest.toolkit.platform.ResourceHelper;
import by.langvest.toolkit.platform.client.RenderHelper;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ForgePlatform extends Platform {
    protected ResourceHelper resourceHelper;
    protected RegistryHelper registryHelper;
    protected RenderHelper renderHelper;

    public ForgePlatform(String modId) {
        super(modId);
        this.resourceHelper = new ForgeResourceHelper(this);
        this.registryHelper = new ForgeRegistryHelper(this);
        this.renderHelper = new ForgeRenderHelper(this);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.FORGE;
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
