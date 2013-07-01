package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.PigZombie;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemotePigmen extends RemoteAttackingBaseEntity<PigZombie>
{
	public RemotePigmen(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemotePigmen(int inID, RemotePigmenEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Pigmen, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "PigZombie";
	}
}