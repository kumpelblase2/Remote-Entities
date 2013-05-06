package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.Ghast;

public class RemoteGhast extends RemoteAttackingBaseEntity<Ghast>
{
	public RemoteGhast(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteGhast(int inID, RemoteGhastEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Ghast, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Ghast";
	}
}
