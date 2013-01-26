package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.Desire;

public interface IEntitySerializer
{
	public EntityData prepare(RemoteEntity inEntity);
	
	public boolean save(EntityData[] inData);
	
	public EntityData[] loadData();
	
	public RemoteEntity create(EntityData inData);

    public Desire create(DesireData inData);
}