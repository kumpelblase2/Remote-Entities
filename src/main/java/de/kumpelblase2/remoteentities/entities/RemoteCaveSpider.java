package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.CaveSpider;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteCaveSpider extends RemoteAttackingBaseEntity<CaveSpider>
{
	public RemoteCaveSpider(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteCaveSpider(int inID, RemoteCaveSpiderEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.CaveSpider, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "CaveSpider";
	}
}