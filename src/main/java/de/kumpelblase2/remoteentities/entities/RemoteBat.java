package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteBat extends RemoteBaseEntity
{
	public RemoteBat(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Bat, inManager);
	}
	
	public RemoteBat(int inID, RemoteBatEntity inEntity, EntityManager inManager)
	{
		this(inID, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		if(this.m_entity == null)
			return;
		
		((RemoteEntityHandle)this.m_entity).setMaxHealth(inMax);
	}

	@Override
	public String getNativeEntityName()
	{
		return "Bat";
	}
}
