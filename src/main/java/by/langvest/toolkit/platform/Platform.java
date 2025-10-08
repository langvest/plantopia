package by.langvest.toolkit.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Platform {
	protected String modId;
	protected Logger logger;
	protected EventEmitter eventEmitter;

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
