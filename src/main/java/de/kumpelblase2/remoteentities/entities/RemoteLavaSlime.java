package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.MagmaCube;

public class RemoteLavaSlime extends RemoteAttackingBaseEntity<MagmaCube>
{
	public RemoteLavaSlime(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteLavaSlime(int inID, RemoteLavaSlimeEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.LavaSlime, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "LavaSlime";
	}
}
