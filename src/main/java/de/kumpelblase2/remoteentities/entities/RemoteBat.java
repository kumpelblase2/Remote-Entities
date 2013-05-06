package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.*;

public class RemoteBat extends RemoteAttackingBaseEntity<Bat>
{
	public RemoteBat(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Bat, inManager);
	}
	
	public RemoteBat(int inID, RemoteBatEntity inEntity, EntityManager inManager)
	{
		this(inID, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Bat";
	}
}
