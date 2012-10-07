package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteSpider extends RemoteBaseEntity implements Fightable
{
	public RemoteSpider(int inID)
	{
		this(inID, null);
	}
	
	public RemoteSpider(int inID, RemoteSpiderEntity inEntity)
	{
		super(inID, RemoteEntityType.Spider);
		this.m_entity = inEntity;
		this.m_speed = 0.2F;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		((RemoteSpiderEntity)this.m_entity).setMaxHealth(inMax);
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
