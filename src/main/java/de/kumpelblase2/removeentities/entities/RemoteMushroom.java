package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteMushroom extends RemoteBaseEntity
{
	public RemoteMushroom(int inID)
	{
		this(inID, null);
	}
	
	public RemoteMushroom(int inID, RemoteMushroomEntity inEntity)
	{
		super(inID, RemoteEntityType.Mushroom);
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
