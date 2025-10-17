package by.langvest.toolkit.platform;

public abstract class PlatformHelper {
	protected final Platform platform;

	public PlatformHelper(Platform platform) {
		this.platform = platform;
	}

	public Platform getPlatform() {
		return platform;
	}
}
