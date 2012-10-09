package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteCow extends RemoteBaseEntity
{
	public RemoteCow(int inID)
	{
		this(inID, null);
	}
	
	public RemoteCow(int inID, RemoteCowEntity inEntity)
	{
		super(inID, RemoteEntityType.Cow);
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
