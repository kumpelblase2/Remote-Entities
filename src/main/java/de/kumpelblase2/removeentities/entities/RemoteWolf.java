package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteWolf extends RemoteBaseEntity implements Fightable
{
	public RemoteWolf(int inID)
	{
		this(inID, null);
	}
	
	public RemoteWolf(int inID, RemoteWolfEntity inEntity)
	{
		super(inID, RemoteEntityType.Wolf);
		this.m_entity = inEntity;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		((RemoteEntityHandle)this.m_entity).setMaxHealth(inMax);
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
}
