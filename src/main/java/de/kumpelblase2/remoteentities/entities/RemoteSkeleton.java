package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_5_R3.EntityLiving;
import org.bukkit.entity.Skeleton;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteSkeleton extends RemoteAttackingBaseEntity<Skeleton>
{	
	public RemoteSkeleton(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteSkeleton(int inID, EntityLiving inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Skeleton, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Skeleton";
	}
}
