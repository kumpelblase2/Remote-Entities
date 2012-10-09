package de.kumpelblase2.removeentities.entities;

import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteChicken extends RemoteBaseEntity
{
	public RemoteChicken(int inID)
	{
		this(inID, null);
	}
	
	public RemoteChicken(int inID, RemoteChickenEntity inEntity)
	{
		super(inID, RemoteEntityType.Chicken);
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
