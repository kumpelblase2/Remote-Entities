package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Zombie;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteZombie extends RemoteAttackingBaseEntity<Zombie>
{	
	public RemoteZombie(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteZombie(int inID, RemoteZombieEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Zombie, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Zombie";
	}
}
