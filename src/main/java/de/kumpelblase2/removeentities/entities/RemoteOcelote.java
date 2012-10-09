package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteOcelote extends RemoteBaseEntity
{
	public RemoteOcelote(int inID)
	{
		this(inID, null);
	}
	
	public RemoteOcelote(int inID, RemoteOceloteEntity inEntity)
	{
		super(inID, RemoteEntityType.Ocelote);
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
