package by.langvest.toolkit.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Platform {
	protected final String modId;

	protected EventEmitter eventEmitter;
	protected Logger logger;

	public Platform(String modId) {
		this.modId = modId;
		this.logger = LogManager.getLogger(modId);
		this.eventEmitter = new EventEmitter();
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

	public abstract String getPlatformName();
}
