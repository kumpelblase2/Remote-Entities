package de.kumpelblase2.remoteentities;

import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

class ChunkEntityLoader implements Listener {
    private EntityManager m_manager;
    public Hashtable<RemoteEntity, Location> toSpawn;

    ChunkEntityLoader(EntityManager inManager) {
        this.m_manager = inManager;
        this.toSpawn = new Hashtable<RemoteEntity, Location>();

        this.populateSpawns();
    }


    class DelayedChunkLoadHandler implements Runnable {
        private ChunkEntityLoader loader;
        private ChunkLoadEvent event;

        DelayedChunkLoadHandler(ChunkEntityLoader loader, ChunkLoadEvent event) {
            this.loader = loader;
            this.event = event;
        }

        @Override
        public void run() {
            Chunk c = this.event.getChunk();

            Iterator entryIterator = this.loader.toSpawn.entrySet().iterator();

            while (entryIterator.hasNext()) {
                Entry<RemoteEntity, Location> toSpawn = (Entry<RemoteEntity, Location>) entryIterator.next();
                Location loc = toSpawn.getValue();
                if (c == loc.getChunk()) {
                    RemoteEntity keyEntity = toSpawn.getKey();
                    keyEntity.spawn(loc);
                    entryIterator.remove();
                }
            }
        }
    }

    class DelayedChunkUnloadHandler implements Runnable {
        private ChunkEntityLoader loader;
        private Chunk chunk;
        private EntityManager manager;
        private Entity[] entitySnapshot;

        DelayedChunkUnloadHandler(ChunkEntityLoader loader, Chunk chunk, Entity[] entitySnapshot, EntityManager manager) {
            this.loader = loader;
            this.chunk = chunk;
            this.manager = manager;
            this.entitySnapshot = entitySnapshot;
        }

        @Override
        public void run() {

            for (Entity entity : this.entitySnapshot) {
                if (!(entity instanceof LivingEntity))
                    continue;


                if (RemoteEntities.isRemoteEntity((LivingEntity) entity)) {
                    RemoteEntity rentity = (RemoteEntity) RemoteEntities.getRemoteEntityFromEntity((LivingEntity) entity);
                    if (rentity.isSpawned()) {
                        this.loader.toSpawn.put(rentity, rentity.getBukkitEntity().getLocation());
                        rentity.despawn(DespawnReason.CHUNK_UNLOAD);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        Bukkit.getScheduler().runTask(this.m_manager.getPlugin(), new DelayedChunkLoadHandler(this, event));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Bukkit.getScheduler().runTask(this.m_manager.getPlugin(), new DelayedChunkUnloadHandler(this, event.getChunk(), event.getChunk().getEntities(), this.m_manager));
    }

    public void populateSpawns() {
        for (RemoteEntity entity : this.m_manager.getAllEntities()) {
            if (!entity.isSpawned() || this.toSpawn.containsKey(entity))
                continue;


            this.toSpawn.put(entity, entity.getBukkitEntity().getLocation());
        }
    }
}
