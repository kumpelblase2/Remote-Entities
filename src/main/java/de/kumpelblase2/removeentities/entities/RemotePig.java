package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemotePig extends RemoteBaseEntity
{
	public RemotePig(int inID)
	{
		this(inID, null);
	}
	
	public RemotePig(int inID, RemotePigEntity inEntity)
	{
		super(inID, RemoteEntityType.Pig);
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
