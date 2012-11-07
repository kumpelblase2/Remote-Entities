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
	
	public static EntityManager createManager(Plugin inPlugin)
	{
		EntityManager manager = new EntityManager(inPlugin);
		getInstance().m_managers.put(inPlugin.getName(), manager);
		return manager;
	}
	
	public static EntityManager getManagerOfPlugin(String inName)
	{
		return getInstance().m_managers.get(inName);
	}
	
	public static boolean hasManagerForPlugin(String inName)
	{
		return getInstance().m_managers.containsKey(inName);
	}
	
	public static RemoteEntities getInstance()
	{
		return s_instance;
	}
	
	public static boolean isRemoteEntity(LivingEntity inEntity)
	{
		for(EntityManager manager : getInstance().m_managers.values())
		{
			if(manager.isRemoteEntity(inEntity))
				return true;
		}
		return false;
	}
	
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
