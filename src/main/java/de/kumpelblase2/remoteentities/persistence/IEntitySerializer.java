package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface IEntitySerializer
{
	public EntityData prepare(RemoteEntity inEntity);
	
	public boolean save(EntityData inData);
	
	public EntityData[] loadData();
	
	public RemoteEntity create(EntityData inData);
}