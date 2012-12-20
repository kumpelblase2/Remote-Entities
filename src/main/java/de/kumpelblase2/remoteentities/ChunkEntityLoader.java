package de.kumpelblase2.remoteentities;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_4_5.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

class ChunkEntityLoader implements Listener
{
	private EntityManager m_manager;
	
	ChunkEntityLoader(EntityManager inManager)
	{
		this.m_manager = inManager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkLoad(ChunkLoadEvent event)
	{
		Chunk c = event.getChunk();
		for(RemoteEntity entity : this.m_manager.getAllEntities())
		{
			if(entity.getBukkitEntity().getLocation().getChunk() == c && entity.getHandle() != null)
				((CraftWorld)c.getWorld()).getHandle().addEntity(entity.getHandle());				
		}
	}
}
