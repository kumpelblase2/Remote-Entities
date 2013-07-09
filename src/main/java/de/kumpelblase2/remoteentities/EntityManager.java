package de.kumpelblase2.remoteentities;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.v1_6_R2.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftLivingEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityCreateEvent;
import de.kumpelblase2.remoteentities.exceptions.NoNameException;
import de.kumpelblase2.remoteentities.persistence.EntityData;
import de.kumpelblase2.remoteentities.persistence.IEntitySerializer;

public class EntityManager
{
	private final Map<Integer, RemoteEntity> m_entities;
	private final Plugin m_plugin;
	protected boolean m_removeDespawned = false;
	private final ChunkEntityLoader m_entityChunkLoader;
	protected IEntitySerializer m_serializer;
	private boolean m_saveOnDisable = false;

	protected EntityManager(final Plugin inPlugin, boolean inRemoveDespawned)
	{
		this.m_plugin = inPlugin;
		this.m_entities = new ConcurrentHashMap<Integer, RemoteEntity>();
		this.m_removeDespawned = inRemoveDespawned;
		this.m_entityChunkLoader = new ChunkEntityLoader(this);
		Bukkit.getPluginManager().registerEvents(this.m_entityChunkLoader, RemoteEntities.getInstance());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(inPlugin, new Runnable()
		{
			@Override
			public void run()
			{
				Iterator<Entry<Integer, RemoteEntity>> it = m_entities.entrySet().iterator();
				while(it.hasNext())
				{
					Entry<Integer, RemoteEntity> entry = it.next();
					RemoteEntity entity = entry.getValue();
					if(entity.getHandle() == null)
					{
						if(m_removeDespawned)
							it.remove();
					}
					else
					{
						entity.getHandle().y();
						if(entity.getHandle().dead)
						{
							if(entity.despawn(DespawnReason.DEATH))
								it.remove();
						}
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

	/**
	 * Gets the next free id starting from 0
	 *
	 * @return	next free id
	 */
	protected Integer getNextFreeID()
	{
		return this.getNextFreeID(0);
	}

	/**
	 * Gets the next free id starting from the id provided.
	 * If the give it is free, it will get returned as well.
	 *
	 * @param inStart	starting id
	 * @return			next free id
	 */
	protected Integer getNextFreeID(int inStart)
	{
		Set<Integer> ids = this.m_entities.keySet();
		while(ids.contains(inStart))
		{
			inStart++;
		}
		return inStart;
	}

	/**
	 * Create an entity at given location with given type.
	 *
	 * @param inType			type of the entity to create
	 * @param inLocation		location where it should get created
	 * @return					the created entity
	 * @throws NoNameException  when trying to spawn a named entity. Use EntityManager#createNamedEntity(RemoteEntityType, Location, String) instead.
	 */
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation)
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
	 * @throws NoNameException  when trying to spawn a named entity. Use EntityManager#createNamedEntity(RemoteEntityType, Location, String, boolean) instead.
	 */
	public RemoteEntity createEntity(RemoteEntityType inType, Location inLocation, boolean inSetupGoals)
	{
		if(inType.isNamed())
			throw new NoNameException("Tried to spawn a named entity without name");

		Integer id = this.getNextFreeID();
		RemoteEntity entity = this.createEntity(inType, id);
		if(entity == null)
			return null;

		if(inLocation != null)
			this.m_entityChunkLoader.queueSpawn(entity, inLocation, inSetupGoals);

		return entity;
	}

	RemoteEntity createEntity(RemoteEntityType inType, int inID)
	{
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(inID, this);
			RemoteEntityCreateEvent event = new RemoteEntityCreateEvent(entity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return null;

			this.m_entities.put(inID, entity);
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
			RemoteEntity entity = this.createEntity(inType, inLocation, inSetupGoals);
			if(entity == null)
				return null;

			entity.setName(inName);
			return entity;
		}

		Integer id = this.getNextFreeID();
		RemoteEntity entity = this.createNamedEntity(inType, id, inName);
		if(entity == null)
			return null;

		if(inLocation != null)
			this.m_entityChunkLoader.queueSpawn(entity, inLocation, inSetupGoals);

		return entity;
	}

	RemoteEntity createNamedEntity(RemoteEntityType inType, int inID, String inName)
	{
		try
		{
			Constructor<? extends RemoteEntity> constructor = inType.getRemoteClass().getConstructor(int.class, String.class, EntityManager.class);
			RemoteEntity entity = constructor.newInstance(inID, inName, this);
			RemoteEntityCreateEvent event = new RemoteEntityCreateEvent(entity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return null;

			this.m_entities.put(inID, entity);
			return entity;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a context that lets you specify more than using the normal methods
	 *
	 * @param inType	Type of the entity
	 * @return			Context that lets you specify the creation parameters
	 */
	public CreateEntityContext prepareEntity(RemoteEntityType inType)
	{
		return new CreateEntityContext(this).withType(inType);
	}

	/**
	 * Removes an entity completely. If the entity is not despawned already, it'll do so.
	 *
	 * @param inID	ID of the entity to remove
	 */
	public void removeEntity(int inID)
	{
		this.removeEntity(inID, true);
	}

	/**
	 * Removes an entity from the list. When inDespawn is true, it'll also try to despawn it.
	 *
	 * @param inID			ID of the entity to remove
	 * @param inDespawn		Whether the entity should get despawned or not
	 */
	public void removeEntity(int inID, boolean inDespawn)
	{
		if(this.m_entities.containsKey(inID) && inDespawn)
			this.m_entities.get(inID).despawn(DespawnReason.CUSTOM);

		this.m_entities.remove(inID);
	}

	/**
	 * Checks whether the provided entity is a RemoteEntity created by this manager
	 *
	 * @param inEntity	entity to check
	 * @return			true if the entity is a RemoteEntity and from this manager, false if not
	 */
	public boolean isRemoteEntity(LivingEntity inEntity)
	{
		if(inEntity.hasMetadata("remoteentity"))
		{
			for(MetadataValue value : inEntity.getMetadata("remoteentity"))
			{
				if(value.getOwningPlugin() == this.m_plugin)
				{
					if(!(value.value() instanceof RemoteEntity))
						continue;

					RemoteEntity e = (RemoteEntity)value.value();
					if(e.getManager() == this)
						return true;
				}
			}
			return false;
		}
		else
		{
			EntityLiving handle = ((CraftLivingEntity)inEntity).getHandle();
			if(!(handle instanceof RemoteEntityHandle))
				return false;

			RemoteEntityHandle h = (RemoteEntityHandle)handle;
			return h.getRemoteEntity().getManager() == this;
		}
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

		if(inEntity.hasMetadata("remoteentity"))
		{
			for(MetadataValue value : inEntity.getMetadata("remoteentity"))
			{
				if(value.getOwningPlugin() == this.m_plugin)
				{
					if(!(value.value() instanceof RemoteEntity))
						continue;

					return (RemoteEntity)value.value();
				}
			}
		}

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
		return this.m_entities.get(inID);
	}

	/**
	 * Returns all entities with the specified name
	 *
	 * @param inName	Name to look for
	 * @return			Entities with that name
	 */
	public RemoteEntity[] getRemoteEntitiesByName(String inName)
	{
		List<RemoteEntity> entities = new ArrayList<RemoteEntity>();

		for(RemoteEntity entity : this.getAllEntities())
		{
			if(entity instanceof Nameable)
			{
				if(((Nameable)entity).getName().equals(inName))
					entities.add(entity);
			}
		}

		return entities.toArray(new RemoteEntity[entities.size()]);
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
	public RemoteEntity createRemoteEntityFromExisting(LivingEntity inEntity)
	{
		return this.createRemoteEntityFromExisting(inEntity, true);
	}

	/**
	 * Creates a RemoteEntity from an existing minecraft entity. The old entity will only get removed when inDeleteOld is true.
	 *
	 * @param inEntity		entity to copy from
	 * @param inDeleteOld	should delete old one
	 * @return				created entity
	 */
	public RemoteEntity createRemoteEntityFromExisting(LivingEntity inEntity, boolean inDeleteOld) //TODO copy more shit from entity
	{
		RemoteEntityType type = RemoteEntityType.getByEntityClass(((CraftLivingEntity)inEntity).getHandle().getClass());
		if(type == null)
			return null;

		Location originalSpot = inEntity.getLocation();
		String name = (inEntity instanceof HumanEntity) ? ((HumanEntity)inEntity).getName() : null;
		if(inDeleteOld)
			inEntity.remove();

		try
		{
			if(name == null)
				return this.createEntity(type, originalSpot, true);
			else
				return this.createNamedEntity(type, originalSpot, name, true);
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
	 * This also saves the entities if the reason is PLUGIN_DISABLE and {@link #shouldSaveOnDisable()} returns true.
	 *
	 * @param inReason	despawn reason
	 */
	public void despawnAll(DespawnReason inReason)
	{
		if(this.m_entities.size() == 0)
			return;

		if(inReason == DespawnReason.PLUGIN_DISABLE)
			this.despawnAll(inReason, this.shouldSaveOnDisable());

		this.despawnAll(inReason, false);
	}

	/**
	 * Despawns all entities from this manager with the given {@link DespawnReason}.
	 * If inSave is true, it will save all entities before despawning them.
	 *
	 * @param inReason	The despawn reason
	 * @param inSave	If the entities should be save before despawning
	 */
	public void despawnAll(DespawnReason inReason, boolean inSave)
	{
		if(inSave)
			this.saveEntities();

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

	/**
	 * Returns the amount of entities registered
	 *
	 * @return	entity count
	 */
	public int getEntityCount()
	{
		return this.m_entities.size();
	}

	/**
	 * Returns whether despawned entities will automatically get removed or not
	 *
	 * @return	true when they get removed, false when not
	 */
	public boolean shouldRemoveDespawnedEntities()
	{
		return this.m_removeDespawned;
	}

	/**
	 * Sets if despawned entities should get automatically get removed
	 *
	 * @param inState	True if they should, false if not
	 */
	public void setRemovingDespawned(boolean inState)
	{
		this.m_removeDespawned = inState;
	}

	void unregisterEntityLoader()
	{
		ChunkLoadEvent.getHandlerList().unregister(this.m_entityChunkLoader);
	}

	/**
	 * Sets the serializer which should be used when saving the entities
	 *
	 * @param inSerializer	serializer to use
	 */
	public void setEntitySerializer(IEntitySerializer inSerializer)
	{
		this.m_serializer = inSerializer;
	}

	/**
	 * Gets the currently used serializer
	 *
	 * @return	serializer
	 */
	public IEntitySerializer getSerializer()
	{
		return this.m_serializer;
	}

	/**
	 * Saves all currently available entities
	 *
	 * @return  The amount of entities saved
	 */
	public int saveEntities()
	{
		if(this.m_serializer == null)
			return 0;

		EntityData[] data = new EntityData[this.m_entities.size()];
		int pos = 0;
		for(RemoteEntity entity : this.m_entities.values())
		{
			data[pos] = this.m_serializer.prepare(entity);
			pos++;
		}
		this.m_serializer.save(data);
		return data.length;
	}

	/**
	 * Loads all saved entities
	 *
	 * @return  The amount of entities loaded
	 */
	public int loadEntities()
	{
		if(this.m_serializer == null)
			return 0;

		EntityData[] data = this.m_serializer.loadData();
		for(EntityData entity : data)
		{
			this.m_serializer.create(entity);
		}

		return data.length;
	}

	/**
	 * Sets whether the entity manager should automatically save the entities when the plugin gets disabled.
	 *
	 * @param inSave	New save state
	 */
	public void setSaveOnDisable(boolean inSave)
	{
		this.m_saveOnDisable = inSave;
	}

	/**
	 * Checks whether entities should be save when the plugin gets disabled.
	 *
	 * @return	state
	 */
	public boolean shouldSaveOnDisable()
	{
		return this.m_saveOnDisable;
	}
}