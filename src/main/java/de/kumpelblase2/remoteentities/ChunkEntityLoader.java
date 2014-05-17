package de.kumpelblase2.remoteentities;

import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

class ChunkEntityLoader implements Listener
{
	private final EntityManager m_manager;
	private final Set<EntityLoadData> m_toSpawn;

	ChunkEntityLoader(EntityManager inManager)
	{
		this.m_manager = inManager;
		this.m_toSpawn = new HashSet<EntityLoadData>();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkLoad(ChunkLoadEvent event)
	{
		final Chunk c = event.getChunk();
		for(RemoteEntity entity : this.m_manager.getAllEntities())
		{
			if(!entity.isSpawned())
				continue;

			if(entity.getBukkitEntity().getLocation().getChunk() == c && entity.getHandle() != null)
				WorldUtilities.updateEntityTracking(entity, c);
		}

		Bukkit.getScheduler().runTask(RemoteEntities.getInstance(), new Runnable()
		{
			public void run()
			{
				Iterator<EntityLoadData> it = m_toSpawn.iterator();
				while(it.hasNext())
				{
					EntityLoadData toSpawn = it.next();
					Location loc = toSpawn.loc;
					if(loc.getChunk() == c)
					{
						spawn(toSpawn);
						it.remove();
					}
				}
			}
		});
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		final Chunk c = event.getChunk();
		for(Entity entity : c.getEntities())
		{
			if(!(entity instanceof LivingEntity))
				continue;

			RemoteEntity rentity = RemoteEntities.getRemoteEntityFromEntity((LivingEntity)entity);
			if(rentity != null && rentity.isSpawned())
			{
				m_toSpawn.add(new EntityLoadData(rentity, entity.getLocation()));
				rentity.despawn(DespawnReason.CHUNK_UNLOAD);
			}
		}
	}

	/**
	 * Checks if an entity can be directly be spawned at given location.
	 *
	 * @param inLocation Location to check for
	 * @return true if it can be spawned, false if not
	 */
	public boolean canSpawnAt(Location inLocation)
	{
		return inLocation.getChunk().isLoaded();
	}

	/**
	 * Queues an entity to spawn whenever the chunk is loaded.
	 *
	 * @param inEntity   Entity to spawn
	 * @param inLocation Location to spawn at
	 * @return true if it gets queued, false if it could be spawned directly
	 */
	public boolean queueSpawn(RemoteEntity inEntity, Location inLocation)
	{
		return this.queueSpawn(inEntity, inLocation, false);
	}

	/**
	 * Queues an entity to spawn whenever the chunk is loaded.
	 *
	 * @param inEntity     Entity to spawn
	 * @param inLocation   Location to spawn at
	 * @param inSetupGoals Whether standard goals should be applied or not
	 * @return true if it gets queued, false if it could be spawned directly
	 */
	public boolean queueSpawn(RemoteEntity inEntity, Location inLocation, boolean inSetupGoals)
	{
		EntityLoadData spawnData = new EntityLoadData(inEntity, inLocation, inSetupGoals);
		if(this.canSpawnAt(inLocation))
		{
			this.spawn(spawnData);
			return false;
		}

		this.m_toSpawn.add(spawnData);
		return true;
	}

	protected void spawn(EntityLoadData inData)
	{
		inData.entity.spawn(inData.loc);
		if(inData.entity.isSpawned() && inData.setupGoals)
			((RemoteEntityHandle)inData.entity.getHandle()).setupStandardGoals();
	}

	class EntityLoadData
	{
		final RemoteEntity entity;
		final Location loc;
		final boolean setupGoals;

		public EntityLoadData(RemoteEntity inEntity, Location inLoc, boolean inSetupGoals)
		{
			this.entity = inEntity;
			this.loc = inLoc;
			this.setupGoals = inSetupGoals;
		}

		public EntityLoadData(RemoteEntity inEntity, Location inLoc)
		{
			this(inEntity, inLoc, false);
		}
	}
}