package de.kumpelblase2.remoteentities;

import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
        Bukkit.getPluginManager().registerEvents(this, this.m_manager.getPlugin());
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
//                    Bukkit.broadcastMessage(Integer.toString(this.loader.toSpawn.size()));
                    entryIterator.remove();
//                    Bukkit.broadcastMessage(Integer.toString(this.loader.toSpawn.size()));
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

                        System.out.println("Despawned " + this.loader.toSpawn.toString());
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
        Chunk chunk = event.getChunk();
        Bukkit.getScheduler().runTask(this.m_manager.getPlugin(), new DelayedChunkUnloadHandler(this, chunk, chunk.getEntities(), this.m_manager));
    }

    public void populateSpawns() {
        for (RemoteEntity entity : this.m_manager.getAllEntities()) {
            Chunk chunk = entity.getBukkitEntity().getLocation().getChunk();
            System.out.println(chunk.getWorld().isChunkLoaded(chunk));

            boolean playerNearby = false;

            for (Entity bEntity : entity.getBukkitEntity().getNearbyEntities(80, 80, 80)) {
                if (!(bEntity instanceof LivingEntity))
                    continue;

                if (bEntity instanceof Player && !RemoteEntities.isRemoteEntity((LivingEntity)bEntity)) {
                    playerNearby = true;
                    System.out.println(bEntity);
                    break;
                }
            }

            if (this.toSpawn.containsKey(entity) || playerNearby) {
                System.out.println("Skipped.");
                continue;
            }



            this.toSpawn.put(entity, entity.getBukkitEntity().getLocation());
            entity.despawn(DespawnReason.CHUNK_UNLOAD);



            System.out.println("Added " + entity.toString());
        }

        Bukkit.broadcastMessage(Integer.toString(this.toSpawn.size()));
    }
}
