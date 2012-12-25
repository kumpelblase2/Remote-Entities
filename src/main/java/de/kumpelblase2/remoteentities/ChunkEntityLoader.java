package de.kumpelblase2.remoteentities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

class ChunkEntityLoader implements Listener
{
	private EntityManager m_manager;
	private Map<RemoteEntity, Location> m_toSpawn;
	
	ChunkEntityLoader(EntityManager inManager)
	{
		this.m_manager = inManager;
		this.m_toSpawn = new HashMap<RemoteEntity, Location>();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkLoad(ChunkLoadEvent event)
	{
		Chunk c = event.getChunk();
		for(RemoteEntity entity : this.m_manager.getAllEntities())
		{
			if(!entity.isSpawned())
				continue;
			
			if(entity.getBukkitEntity().getLocation().getChunk() == c && entity.getHandle() != null)
				((CraftWorld)c.getWorld()).getHandle().addEntity(entity.getHandle());				
		}
		
		Iterator<Entry<RemoteEntity, Location>> it = this.m_toSpawn.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<RemoteEntity, Location> toSpawn = it.next();
			Location loc = toSpawn.getValue();
			if(loc.getChunk() == c)
			{
				toSpawn.getKey().spawn(loc);
				it.remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		Chunk c = event.getChunk();
		for(Entity entity : c.getEntities())
		{
			if(!(entity instanceof LivingEntity))
				continue;
			
			if(RemoteEntities.isRemoteEntity((LivingEntity)entity))
			{
				RemoteEntity rentity = (RemoteEntity)RemoteEntities.getRemoteEntityFromEntity((LivingEntity)entity);
				if(rentity.isSpawned())
				{
					this.m_toSpawn.put(rentity, rentity.getBukkitEntity().getLocation());
					rentity.despawn(DespawnReason.CHUNK_UNLOAD);
				}
			}
		}
	}
}
