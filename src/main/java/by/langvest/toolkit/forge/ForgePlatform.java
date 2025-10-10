package by.langvest.toolkit.forge;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.RegistryHelper;
import by.langvest.toolkit.platform.ResourceHelper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

public class ForgePlatform extends Platform {
	protected static final String PLATFORM_NAME = "forge";
	protected FMLJavaModLoadingContext context;
	protected ResourceHelper resourceHelper;
	protected RegistryHelper registryHelper;

	public ForgePlatform(String modId, @NotNull FMLJavaModLoadingContext context) {
		super(modId);
		this.context = context;
		this.resourceHelper = new ForgeResourceHelper(this);
		this.registryHelper = new ForgeRegistryHelper(this);
	}

	public FMLJavaModLoadingContext getContext() {
		return context;
	}

	@Override
	public String getPlatformName() {
		return PLATFORM_NAME;
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
	public ResourceHelper getResourceHelper() {
		return resourceHelper;
	}

	@Override
	public RegistryHelper getRegistryHelper() {
		return registryHelper;
	}
}
