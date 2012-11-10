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
import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteEntities extends JavaPlugin
{
	private Map<String, EntityManager> m_managers = new HashMap<String, EntityManager>();
	private static RemoteEntities s_instance;
	
	@Override
	public void onEnable()
	{
		s_instance = this;
		Bukkit.getPluginManager().registerEvents(new DisableListener(), this);
	}
	
	@Override
	public void onDisable()
	{
		s_instance = null;
	}
	
	/**
	 * Creates a manager for your plugin
	 * 
	 * @param inPlugin	plugin using that manager
	 * @return			instance of a manager
	 */
	public static EntityManager createManager(Plugin inPlugin)
	{
		EntityManager manager = new EntityManager(inPlugin);
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
		return getInstance().m_managers.containsKey(inName);
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
		for(EntityManager manager : getInstance().m_managers.values())
		{
			RemoteEntity entity = manager.getRemoteEntityFromEntity(inEntity);
			if(entity != null)
				return entity;
		}
		return null;
	}
	
	class DisableListener implements Listener
	{
		@EventHandler
		public void onPluginDisable(PluginDisableEvent event)
		{
			EntityManager manager = RemoteEntities.getManagerOfPlugin(event.getPlugin().getName());
			if(manager != null)
				manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
		}
	}
}
