package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteSheep extends RemoteBaseEntity
{
	public RemoteSheep(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteSheep(int inID, RemoteSheepEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Sheep, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Sheep";
	}
}
