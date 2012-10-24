package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.EntityManager;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemotePig extends RemoteBaseEntity
{
	public RemotePig(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemotePig(int inID, RemotePigEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Pig, inManager);
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
