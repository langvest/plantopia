package by.langvest.plantopia.extension;

public interface PlantopiaZombieQuicksandExtension {
	boolean plantopia$convertsInQuicksand();

	boolean plantopia$isInQuicksandConverting();

	int plantopia$getInQuicksandTime();

	void plantopia$setInQuicksandTime(int inQuicksandTime);

	int plantopia$getQuicksandConversationTime();

	void plantopia$startQuicksandConversion(int quicksandConversationTime);

	void plantopia$doQuicksandConversion();
}
