package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteSquid extends RemoteBaseEntity
{
	public RemoteSquid(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteSquid(int inID, RemoteSquidEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Squid, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Squid";
	}
}
