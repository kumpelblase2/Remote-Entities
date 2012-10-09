package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteVillager extends RemoteBaseEntity
{
	public RemoteVillager(int inID)
	{
		this(inID, null);
	}
	
	public RemoteVillager(int inID, RemoteVillagerEntity inEntity)
	{
		super(inID, RemoteEntityType.Villager);
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
}
