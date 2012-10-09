package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteSquid extends RemoteBaseEntity
{
	public RemoteSquid(int inID)
	{
		this(inID, null);
	}
	
	public RemoteSquid(int inID, RemoteSquidEntity inEntity)
	{
		super(inID, RemoteEntityType.Squid);
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
