package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteVillager extends RemoteBaseEntity
{
	public RemoteVillager(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteVillager(int inID, RemoteVillagerEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Villager, inManager);
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
