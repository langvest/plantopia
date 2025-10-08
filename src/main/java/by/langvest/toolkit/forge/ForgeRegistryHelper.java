package by.langvest.toolkit.forge;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.RegistryHelper;

public class ForgeRegistryHelper extends RegistryHelper {
	public ForgeRegistryHelper(Platform platform) {
		super(platform);
	}

//	protected void addForgeRegistries(Map<ResourceLocation, RegistryEntry> registries) {
//		BiConsumer<Field, ResourceLocation> func = (field, location) -> {
//			Type genericType = field.getGenericType();
//
//			if(genericType instanceof ParameterizedType registryType) {
//				Type[] registryTypeArgs = registryType.getActualTypeArguments();
//
//				if(registryTypeArgs.length == 1) {
//					registries.put(location, new RegistryEntry(
//						location,
//						getErasedClassFromType(registryTypeArgs[0]),
//						() -> forgeAdapter
//					);
//				}
//			}
//		};
//
//		for(Field field : ForgeRegistries.class.getDeclaredFields()) {
//			try {
//				if(!Modifier.isStatic(field.getModifiers())) continue;
//
//				field.setAccessible(true);
//				var value = field.get(null);
//
//				if(value instanceof IForgeRegistry<?> forgeRegistry) {
//					func.accept(field, forgeRegistry.getRegistryName());
//				}
//
//				if(value instanceof DeferredRegister<?> deferredRegister) {
//					func.accept(field, deferredRegister.getRegistryName());
//				}
//			} catch(Exception e) {
//				platform.getLogger().error("Error while obtaining forge registry", e);
//			}
//		}
//	}
}
