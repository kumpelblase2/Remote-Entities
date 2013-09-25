package de.kumpelblase2.remoteentities;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.exceptions.PluginNotEnabledException;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteEntities extends JavaPlugin
{
	private final Map<String, EntityManager> m_managers = new HashMap<String, EntityManager>();
	private static RemoteEntities s_instance;
	private static final String COMPATIBLE_VERSION = "1.6.4";
	private static final String COMPATIBLE_REVISION = "1_6_R3";
	private static String MINECRAFT_REVISION;

	@Override
	public void onEnable()
	{
		MINECRAFT_REVISION = ReflectionUtil.getMinecraftRevision();
		String minecraftversion = this.getPresentMinecraftVersion();
		if(!minecraftversion.equals(COMPATIBLE_VERSION) && !MINECRAFT_REVISION.equals(COMPATIBLE_REVISION)){
			this.getLogger().severe("Invalid minecraft version for remote entities (Required: " + COMPATIBLE_VERSION + " ; Present: " + minecraftversion + ").");
			this.getLogger().severe("Disabling plugin to prevent issues.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		s_instance = this;
		RemoteEntityType.update();
		Bukkit.getPluginManager().registerEvents(new DisableListener(), this);
	}

	@Override
	public void onDisable()
	{
		for(EntityManager manager : m_managers.values())
		{
			manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
			manager.unregisterEntityLoader();
		}
		s_instance = null;
	}

	private String getPresentMinecraftVersion()
	{
		String fullVersion = Bukkit.getServer().getVersion();
		String[] split = fullVersion.split("MC: ");
		split = split[1].split("\\)");

		return split[0];
	}

	/**
	 * Creates a manager for your plugin
	 *
	 * @param inPlugin	plugin using that manager
	 * @return			instance of a manager
	 */
	public static EntityManager createManager(Plugin inPlugin) throws PluginNotEnabledException
	{
		if(getInstance() == null)
			throw new PluginNotEnabledException();

		return createManager(inPlugin, false);
	}

	/**
	 * Creates a manager for your plugin
	 * You can also specify whether despawned entities should be removed or not
	 *
	 * @param inPlugin				plugin using that manager
	 * @param inRemoveDespawned		automatically removed despawned entities
	 * @return						instance of a manager
	 */
	public static EntityManager createManager(Plugin inPlugin, boolean inRemoveDespawned)
	{
		if(getInstance() == null)
			throw new PluginNotEnabledException();

		EntityManager manager = new EntityManager(inPlugin, inRemoveDespawned);
		registerCustomManager(manager, inPlugin.getName());
		return manager;
	}

	/**
	 * Adds custom created manager to internal map
	 *
	 * @param inManager custom manager
	 * @param inName	name of the plugin using it
	 */
	public static void registerCustomManager(EntityManager inManager, String inName)
	{
		if(getInstance() == null)
			return;

		getInstance().m_managers.put(inName, inManager);
	}

	/**
	 * Gets the manager of a specific plugin
	 *
	 * @param inName	name of the plugin
	 * @return			EntityManager of that plugin
	 */
	public static EntityManager getManagerOfPlugin(String inName)
	{
		if(getInstance() == null)
			return null;

		return getInstance().m_managers.get(inName);
	}

	/**
	 * Checks if a specific plugin has registered a manager
	 *
	 * @param inName	name of the plugin
	 * @return			true if the given plugin has a manager, false if not
	 */
	public static boolean hasManagerForPlugin(String inName)
	{
		return getInstance() != null && getInstance().m_managers.containsKey(inName);
	}

	/**
	 * Gets the instance of the RemoteEntities plugin
	 *
	 * @return RemoteEntities
	 */
	public static RemoteEntities getInstance()
	{
		return s_instance;
	}

	/**
	 * Checks if the given entity is a RemoteEntity created by any manager.
	 *
	 * @param inEntity	entity to check
	 * @return			true if it is a RemoteEntity, false if not
	 */
	public static boolean isRemoteEntity(LivingEntity inEntity)
	{
		if(getInstance() == null)
			return false;

		for(EntityManager manager : getInstance().m_managers.values())
		{
			if(manager.isRemoteEntity(inEntity))
				return true;
		}
		return false;
	}

	/**
	 * Gets the RemoteEntity from a given entity which can be created by any manager.
	 *
	 * @param inEntity	entity
	 * @return			RemoteEntity
	 */
	public static RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		if(getInstance() == null)
			return null;

		for(EntityManager manager : getInstance().m_managers.values())
		{
			RemoteEntity entity = manager.getRemoteEntityFromEntity(inEntity);
			if(entity != null)
				return entity;
		}
		return null;
	}

	/**
	 * Returns the minecraft version this version of remote entities is compatible with.
	 *
	 * @return  A string representing the version
	 */
	public static String getCompatibleMinecraftVersion()
	{
		return COMPATIBLE_VERSION;
	}

	public static String getMinecraftRevision()
	{
		return MINECRAFT_REVISION;
	}

	class DisableListener implements Listener
	{
		@EventHandler
		public void onPluginDisable(PluginDisableEvent event)
		{
			EntityManager manager = RemoteEntities.getManagerOfPlugin(event.getPlugin().getName());
			if(manager != null)
			{
				manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
				manager.unregisterEntityLoader();
			}
		}
	}
}