package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteMushroom extends RemoteBaseEntity
{
	public RemoteMushroom(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteMushroom(int inID, RemoteMushroomEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Mushroom, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		if(this.m_entity == null)
			return;
		
		((RemoteEntityHandle)this.m_entity).setMaxHealth(inMax);
	}

	@Override
	public String getNativeEntityName()
	{
		return "MushroomCow";
	}
}
