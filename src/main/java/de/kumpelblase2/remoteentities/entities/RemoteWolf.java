package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Wolf;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteWolf extends RemoteAttackingBaseEntity<Wolf>
{
	public RemoteWolf(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteWolf(int inID, RemoteWolfEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Wolf, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Wolf";
	}
}
