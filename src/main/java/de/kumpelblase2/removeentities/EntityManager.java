package de.kumpelblase2.removeentities;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.minecraft.server.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.removeentities.entities.RemoteEntity;
import de.kumpelblase2.removeentities.entities.RemoteEntityHandle;
import de.kumpelblase2.removeentities.entities.RemoteEntityType;
import de.kumpelblase2.removeentities.exceptions.NoNameExeption;

public class EntityManager
{
	private static EntityManager m_instance;
	private Map<Integer, RemoteEntity> m_entities;
	private Plugin m_plugin;
	
	public EntityManager(Plugin inPlugin)
	{
		m_instance = this;
		this.m_plugin = inPlugin;
		this.m_entities = new HashMap<Integer, RemoteEntity>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(inPlugin, new Runnable()
		{
			@Override
			public void run()
			{
				Iterator<Entry<Integer, RemoteEntity>> it = m_entities.entrySet().iterator();
				while(it.hasNext())
				{
					Entry<Integer, RemoteEntity> entry = it.next();
					entry.getValue().getHandle().z();
					if(entry.getValue().getHandle().dead)
					{
						entry.getValue().despawn();
						it.remove();
					}
				}
			}
		}, 1L, 1L);
	}
	
	public static EntityManager getInstance()
	{
		return m_instance;
	}
	
	public Plugin getPlugin()
	{
		return this.m_plugin;
	}
	
	private Integer getNextFreeID()
	{
		Set<Integer> ids = this.m_entities.keySet();
		Integer current = 0;
		while(ids.contains(current))
		{
			current++;
		}
		return current;
	}
	
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation) throws NoNameExeption
	{
		if(inType.isNamed())
			throw new NoNameExeption("Tried to spawn a named entity without name");

		Integer id = this.getNextFreeID();
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class);
			RemoteEntity entity = constructor.newInstance(id);
			entity.spawn(inLocation);
			this.m_entities.put(id, entity);
			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public RemoteEntity createNamedEntity(RemoteEntityType inType, Location inLocation, String inName)
	{
		Integer id = this.getNextFreeID();
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, String.class);
			RemoteEntity entity = constructor.newInstance(id, inName);
			entity.spawn(inLocation);
			this.m_entities.put(id, entity);
			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void removeEntity(int inID)
	{
		if(this.m_entities.containsKey((Integer)inID))
			this.m_entities.get((Integer)inID).despawn();
		
		this.m_entities.remove((Integer)inID);
	}
	
	public boolean isRemoteEntity(LivingEntity inEntity)
	{
		EntityLiving handle = ((CraftLivingEntity)inEntity).getHandle();
		return handle instanceof RemoteEntityHandle;
	}
	
	public RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		if(!this.isRemoteEntity(inEntity))
			return null;
		
		EntityLiving entityHandle = ((CraftLivingEntity)inEntity).getHandle();
		return ((RemoteEntityHandle)entityHandle).getRemoteEntity();
	}
	
	public RemoteEntity getRemoteEntityByID(int inID)
	{
		return this.m_entities.get((Integer)inID);
	}
	
	public void addRemoteEntity(int inID, RemoteEntity inEntity)
	{
		this.m_entities.put(inID, inEntity);
	}
}
