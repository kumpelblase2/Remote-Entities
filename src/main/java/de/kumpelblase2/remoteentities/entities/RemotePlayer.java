package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.RemoteEntitySpawnEvent;

public class RemotePlayer extends RemoteBaseEntity implements RemoteEntity, Nameable, Fightable
{
	protected String m_name;
	
	public RemotePlayer(int inID, String inName, EntityManager inManager)
	{
		this(inID, inName, null, inManager);
	}
	
	public RemotePlayer(int inID, String inName, RemotePlayerEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Human, inManager);
		this.m_name = inName;
		this.m_entity = inEntity;
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
	}

	@Override
	public void loseTarget()
	{
	}

	@Override
	public String getName()
	{
		return this.m_name;
	}

	@Override
	public void setName(String inName)
	{
		this.m_name = inName;
		Location loc = this.getBukkitEntity().getLocation();
		this.despawn(DespawnReason.NAME_CHANGE);
		this.spawn(loc);
	}
	
	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;
		
		RemoteEntitySpawnEvent event = new RemoteEntitySpawnEvent(this, inLocation);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		inLocation = event.getSpawnLocation();
		
		WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
		this.m_entity = new RemotePlayerEntity(worldServer.getMinecraftServer(), worldServer, this.getName(), new ItemInWorldManager(worldServer), this);
		worldServer.addEntity(m_entity);
		this.m_entity.getBukkitEntity().teleport(inLocation);
		this.m_entity.world.players.remove(this.m_entity);
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		this.getHandle().setMaxHealth(inMax);
	}
	
	@Override
	public RemotePlayerEntity getHandle()
	{
		return (RemotePlayerEntity)this.m_entity;
	}

	@Override
	public LivingEntity getTarget()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
