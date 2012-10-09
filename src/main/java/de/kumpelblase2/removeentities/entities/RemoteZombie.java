package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.api.*;

public class RemoteZombie extends RemoteBaseEntity implements RemoteEntity, Fightable
{	
	public RemoteZombie(int inID)
	{
		this(inID, null);
	}
	
	public RemoteZombie(int inID, RemoteZombieEntity inEntitiy)
	{
		super(inID, RemoteEntityType.Zombie);
		this.m_entity = inEntitiy;
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
