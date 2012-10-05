package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import de.kumpelblase2.removeentities.thinking.Behaviour;

public class RemoteZombie extends RemoteBaseEntity implements RemoteEntity, Fightable
{
	protected RemoteZombieEntity m_entity;
	
	public RemoteZombie(int inID)
	{
		super(inID, RemoteEntityType.Zombie);
	}
	
	public RemoteZombie(int inID, RemoteZombieEntity inEntitiy)
	{
		this(inID);
		this.m_entity = inEntitiy;
	}

	@Override
	public LivingEntity getBukkitEntity()
	{
		return (LivingEntity)this.m_entity.getBukkitEntity();
	}

	@Override
	public void move(Location inLocation)
	{
	}

	@Override
	public void teleport(Location inLocation)
	{
	}

	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;
		
		WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
		this.m_entity = new RemoteZombieEntity(worldServer, this);
		this.m_entity.setPositionRotation(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inLocation.getYaw(), inLocation.getPitch());
		worldServer.addEntity(m_entity, SpawnReason.CUSTOM);
	}

	@Override
	public void despawn()
	{
		for(Behaviour behaviour : this.getMind().getBehaviours())
		{
			behaviour.onRemove();
		}
		this.getMind().clearBehaviours();
		this.getBukkitEntity().remove();
		this.m_entity = null;
	}

	@Override
	public boolean isSpawned()
	{
		return this.m_entity != null;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		
	}

	@Override
	public int getMaxHealth()
	{
		return this.m_entity.getMaxHealth();
	}

	@Override
	public float getSpeed()
	{
		return 0;
	}

	@Override
	public void setSpeed(float inSpeed)
	{
	}

	@Override
	public boolean isPushable()
	{
		return false;
	}

	@Override
	public void setPushable(boolean inState)
	{
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
		return this.m_entity;
	}
}
