package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteCaveSpider extends RemoteBaseEntity implements Fightable
{
	public RemoteCaveSpider(int inID)
	{
		this(inID, null);
	}
	
	public RemoteCaveSpider(int inID, RemoteCaveSpiderEntity inEntity)
	{
		super(inID, RemoteEntityType.CaveSpider);
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
}
