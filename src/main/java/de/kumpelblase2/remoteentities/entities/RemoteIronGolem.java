package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.IronGolem;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteIronGolem extends RemoteAttackingBaseEntity<IronGolem>
{
	public RemoteIronGolem(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteIronGolem(int inID, RemoteIronGolemEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.IronGolem, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "VillagerGolem";
	}
}