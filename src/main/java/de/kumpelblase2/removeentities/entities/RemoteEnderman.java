package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.EntityManager;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteEnderman extends RemoteBaseEntity implements Fightable
{
	public RemoteEnderman(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteEnderman(int inID, RemoteEndermanEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Enderman, inManager);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loseTarget()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public LivingEntity getTarget()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
