package de.kumpelblase2.remoteentities;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;
import net.minecraft.server.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.exceptions.NoNameException;

public class EntityManager
{
	private Map<Integer, RemoteEntity> m_entities;
	private final Plugin m_plugin;
	
	protected EntityManager(final Plugin inPlugin)
	{
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
					entry.getValue().getHandle().y();
					if(entry.getValue().getHandle().dead)
					{
						entry.getValue().despawn(DespawnReason.DEATH);
						it.remove();
					}
				}
			}
		}, 1L, 1L);
	}
	
	/**
	 * Gets the plugin that created this EntityManager
	 * 
	 * @return	the plugin
	 */
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
	
	/**
	 * Create an entity at given location with given type.
	 * 
	 * @param inType			type of the entity to create
	 * @param inLocation		location where it should get created
	 * @return					the created entity
	 * @throws NoNameException	when trying to create a named entity. Use {@link EntityManager#createNamedEntity(RemoteEntityType, Location, String)} instead
	 */
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation) throws NoNameException
	{
		return this.createEntity(inType, inLocation, true);
	}
	
	/**
	 * Creates an entity at given location with given type.
	 * You can also specify if you want to setup the default desires/goals of the entity (default is true).
	 * 
	 * @param inType			type of the entity to create
	 * @param inLocation		location where it should get created
	 * @param inSetupGoals		if default desires/goals should be applied
	 * @return					the created entity
	 * @throws NoNameException	when trying to create a named entity. Use {@link EntityManager#createNamedEntity(RemoteEntityType, Location, String, boolean)} instead
	 */
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation, boolean inSetupGoals) throws NoNameException
	{
		if(inType.isNamed())
			throw new NoNameException("Tried to spawn a named entity without name");

		Integer id = this.getNextFreeID();
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(id, this);
			entity.spawn(inLocation);
			if(inSetupGoals)
				((RemoteEntityHandle)entity.getHandle()).setupStandardGoals();
			this.m_entities.put(id, entity);
			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Creates a named entity at given location with given type.
	 * 
	 * @param inType		type of the entity to create
	 * @param inLocation	location where it should get created
	 * @param inName		name of the entity
	 * @return				the created entity
	 */
	public RemoteEntity createNamedEntity(RemoteEntityType inType, Location inLocation, String inName)
	{
		if(!inType.isNamed())
		{
			try
			{
				return this.createEntity(inType, inLocation);
			}
			catch(NoNameException e)
			{
				return null;
			}
		}
		return this.createNamedEntity(inType, inLocation, inName, true);
	}
	
	/**
	 * Creates a named entity at given location with given type.
	 * 
	 * @param inType		type of the entity to create
	 * @param inLocation	location where it should get created
	 * @param inName		name of the entity
	 * @param inSetupGoals	if default goals/desires should be applied
	 * @return				the created entity
	 */
	public RemoteEntity createNamedEntity(RemoteEntityType inType, Location inLocation, String inName, boolean inSetupGoals)
	{
		if(!inType.isNamed())
		{
			try
			{
				return this.createEntity(inType, inLocation, inSetupGoals);
			}
			catch(NoNameException e)
			{
				return null;
			}
		}
		
		Integer id = this.getNextFreeID();
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, String.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(id, inName, this);
			entity.spawn(inLocation);
			if(inSetupGoals)
				((RemoteEntityHandle)entity.getHandle()).setupStandardGoals();
			this.m_entities.put(id, entity);
			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Removes an entity completely. If the entity is not despawned already, it'll do so.
	 * 
	 * @param inID	ID of the entity to remove
	 */
	public void removeEntity(int inID)
	{
		if(this.m_entities.containsKey((Integer)inID))
			this.m_entities.get((Integer)inID).despawn(DespawnReason.CUSTOM);
		
		this.m_entities.remove((Integer)inID);
	}
	
	/**
	 * Checks whether the provided entity is a RemoteEntity created by this manager
	 * 
	 * @param inEntity	entity to check
	 * @return			true if the entity is a RemoteEntity, false if not
	 */
	public boolean isRemoteEntity(LivingEntity inEntity)
	{
		EntityLiving handle = ((CraftLivingEntity)inEntity).getHandle();
		return handle instanceof RemoteEntityHandle;
	}
	
	/**
	 * Gets the existing RemoteEntity from the provided entity.
	 * 
	 * @param inEntity	entity
	 * @return			instance of the RemoteEntity
	 */
	public RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		if(!this.isRemoteEntity(inEntity))
			return null;
		
		EntityLiving entityHandle = ((CraftLivingEntity)inEntity).getHandle();
		return ((RemoteEntityHandle)entityHandle).getRemoteEntity();
	}
	
	/**
	 * Gets the RemoteEntity by its ID
	 * 
	 * @param inID	ID of the entity
	 * @return		RemoteEntity with given ID
	 */
	public RemoteEntity getRemoteEntityByID(int inID)
	{
		return this.m_entities.get((Integer)inID);
	}
	
	/**
	 * Adds an already existing RemoteEntity to the manager
	 * 
	 * @param inID		ID of the entity
	 * @param inEntity	entity to add
	 */
	public void addRemoteEntity(int inID, RemoteEntity inEntity)
	{
		this.m_entities.put(inID, inEntity);
	}
	
	/**
	 * Creates a RemoteEntity from an existing minecraft entity. The old entity will be replaced with the RemoteEntity
	 * 
	 * @param inEntity	entity to replace
	 * @return			instance of the RemoteEntity
	 */
	public RemoteEntity createRemoteEntityFromExisting(LivingEntity inEntity) //TODO copy more shit from entity
	{
		RemoteEntityType type = RemoteEntityType.getByEntityClass(((CraftLivingEntity)inEntity).getHandle().getClass());
		if(type == null)
			return null;
		
		Location originalSpot = inEntity.getLocation();
		String name = (inEntity instanceof HumanEntity) ? ((HumanEntity)inEntity).getName() : null;
		inEntity.remove();
		try
		{
			if(name == null)
				return this.createEntity(type, originalSpot);
			else
				return this.createNamedEntity(type, originalSpot, name);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * Despawns all entities from this manager 
	 */
	public void despawnAll()
	{
		this.despawnAll(DespawnReason.CUSTOM);
	}
	
	/**
	 * Despawns all entities from this manager with the given {@link DespawnReason}.
	 * 
	 * @param inReason	despawn reason
	 */
	public void despawnAll(DespawnReason inReason)
	{
		for(RemoteEntity entity : this.m_entities.values())
		{
			entity.despawn(inReason);
		}
		this.m_entities.clear();
	}
	
	/**
	 * Gets all entities created by this manager
	 * 
	 * @return	list of all entities
	 */
	public List<RemoteEntity> getAllEntities()
	{
		return new ArrayList<RemoteEntity>(this.m_entities.values());
	}
}