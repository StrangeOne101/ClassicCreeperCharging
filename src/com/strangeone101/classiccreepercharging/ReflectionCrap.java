package com.strangeone101.classiccreepercharging;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;

import com.projectkorra.projectkorra.util.ReflectionHandler;
import com.projectkorra.projectkorra.util.ReflectionHandler.PackageType;

public class ReflectionCrap {
	
	private static final String version;
	private static Class<?> clazzCraftEntity;
	private static Method getHandle;
	private static Method dataWatcherSet;
	private static Field dataWatcher;
	private static Field c;
	private static Field maxFuseTicks;
	
	private static ClassicCreeperCharging INSTANCE;
	
	static {
		version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
		
		try {
			clazzCraftEntity = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftEntity");
			getHandle = clazzCraftEntity.getMethod("getHandle");
			c = ReflectionHandler.getField("EntityCreeper", PackageType.MINECRAFT_SERVER, true, "c");
			dataWatcher = ReflectionHandler.getField("Entity", PackageType.MINECRAFT_SERVER, true, "datawatcher");
			maxFuseTicks = ReflectionHandler.getField("EntityCreeper", PackageType.MINECRAFT_SERVER, true, "maxFuseTicks");
			Class<?> dataWatcherClass = dataWatcher.getType();
			for (Method m : dataWatcherClass.getDeclaredMethods()) {
				if (m.getName().equals("set")) {
					dataWatcherSet = m; //Easier than finding declared field that takes T
					break;
				}
			}
			if (dataWatcherSet == null) {
				throw new NoSuchFieldException("Could not locate set method within the DataWatcher class!");
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			INSTANCE.getLogger().severe("Failed to find fields and methods to use in creeper class! On version " + version);
			e.printStackTrace();
			INSTANCE.getPluginLoader().disablePlugin(INSTANCE);
		}
	}
	
	public static void explodeCreeper(Creeper creeper) {
		try {
			final Object craftCreeper = getHandle.invoke(clazzCraftEntity.cast(creeper));
			final Object dataWatcherObject = dataWatcher.get(craftCreeper);
			final Object cObject = c.get(craftCreeper);
			maxFuseTicks.setInt(craftCreeper, 1);
			dataWatcherSet.invoke(dataWatcherObject, cObject, Boolean.valueOf(true));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			INSTANCE.getLogger().severe("Failed to ignite creeper! On version " + version);
			e.printStackTrace();
		} 
	}
	
	public static void setPlugin(ClassicCreeperCharging plugin) {
		INSTANCE = plugin;
	}

}
