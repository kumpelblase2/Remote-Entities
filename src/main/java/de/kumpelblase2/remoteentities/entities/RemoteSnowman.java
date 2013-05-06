package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.Snowman;

public class RemoteSnowman extends RemoteAttackingBaseEntity<Snowman>
{
	public RemoteSnowman(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteSnowman(int inID, RemoteSnowmanEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Snowman, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "SnowMan";
	}
}
