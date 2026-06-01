package by.langvest.toolkit.neoforge;

import by.langvest.toolkit.neoforge.client.NeoForgeRenderHelper;
import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.PlatformType;
import by.langvest.toolkit.platform.RegistryHelper;
import by.langvest.toolkit.platform.ResourceHelper;
import by.langvest.toolkit.platform.client.RenderHelper;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import org.jetbrains.annotations.NotNull;

public class NeoForgePlatform extends Platform {
    protected FMLJavaModLoadingContext context;
    protected ResourceHelper resourceHelper;
    protected RegistryHelper registryHelper;
    protected RenderHelper renderHelper;

    public NeoForgePlatform(String modId, @NotNull FMLJavaModLoadingContext context) {
        super(modId);
        this.context = context;
        this.resourceHelper = new NeoForgeResourceHelper(this);
        this.registryHelper = new NeoForgeRegistryHelper(this);
        this.renderHelper = new NeoForgeRenderHelper(this);
    }

    public FMLJavaModLoadingContext getContext() {
        return context;
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
