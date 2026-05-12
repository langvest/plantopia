package by.langvest.toolkit.platform;

import by.langvest.toolkit.platform.client.RenderHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Platform {
	protected String modId;
	protected Logger logger;
	protected EventEmitter eventEmitter;
	protected WorkScheduler workScheduler;

	public Platform(String modId) {
		this.modId = modId;
		this.logger = LogManager.getLogger(modId);
		this.eventEmitter = new EventEmitter();
		this.workScheduler = new WorkScheduler();
	}

	public String getModId() {
		return modId;
	}

	public Logger getLogger() {
		return logger;
	}

	public EventEmitter getEventEmitter() {
		return eventEmitter;
	}

	public WorkScheduler getWorkScheduler() {
		return workScheduler;
	}

	public abstract String getPlatformName();

	public abstract boolean isClient();

	public abstract boolean isServer();

	public abstract ResourceHelper getResourceHelper();

	public abstract RegistryHelper getRegistryHelper();

	public abstract RenderHelper getRenderHelper();
}
