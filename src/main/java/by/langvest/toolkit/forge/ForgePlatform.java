package by.langvest.toolkit.forge;

import by.langvest.toolkit.platform.Platform;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

public class ForgePlatform extends Platform {
	protected static final String PLATFORM_NAME = "forge";
	protected FMLJavaModLoadingContext context;

	public ForgePlatform(String modId, @NotNull FMLJavaModLoadingContext context) {
		super(modId);
		this.context = context;
	}

	public FMLJavaModLoadingContext getContext() {
		return context;
	}

	@Override
	public String getPlatformName() {
		return PLATFORM_NAME;
	}
}
