package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import de.kumpelblase2.removeentities.api.*;

public class RemoteZombie extends RemoteBaseEntity implements RemoteEntity, Fightable
{	
	public RemoteZombie(int inID)
	{
		super(inID, RemoteEntityType.Zombie);
	}
	
	public RemoteZombie(int inID, RemoteZombieEntity inEntitiy)
	{
		this(inID);
		this.m_entity = inEntitiy;
		this.m_speed = 0.2F;
	}

	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;
		
		WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
		this.m_entity = new RemoteZombieEntity(worldServer, this);
		this.m_entity.setPositionRotation(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inLocation.getYaw(), inLocation.getPitch());
		worldServer.addEntity(this.m_entity, SpawnReason.CUSTOM);
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		((RemoteZombieEntity)this.m_entity).setMaxHealth(inMax);
	}

	@Override
	public int getMaxHealth()
	{
		return this.m_entity.getMaxHealth();
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
	public RemoteZombieEntity getHandle()
	{
		return (RemoteZombieEntity)this.m_entity;
	}
}
