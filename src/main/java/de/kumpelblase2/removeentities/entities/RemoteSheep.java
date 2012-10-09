package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteSheep extends RemoteBaseEntity
{
	public RemoteSheep(int inID)
	{
		this(inID, null);
	}
	
	public RemoteSheep(int inID, RemoteSheepEntity inEntity)
	{
		super(inID, RemoteEntityType.Sheep);
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
