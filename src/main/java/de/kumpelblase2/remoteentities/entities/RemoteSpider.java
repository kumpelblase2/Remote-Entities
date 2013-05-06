package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.Spider;

public class RemoteSpider extends RemoteAttackingBaseEntity<Spider>
{
	public RemoteSpider(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteSpider(int inID, RemoteSpiderEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Spider, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Spider";
	}
}
