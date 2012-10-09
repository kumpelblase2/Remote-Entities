package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.EntityLiving;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteSkeleton extends RemoteBaseEntity implements Fightable
{	
	public RemoteSkeleton(int inID)
	{
		this(inID, null);
	}
	
	public RemoteSkeleton(int inID, EntityLiving inEntity)
	{
		super(inID, RemoteEntityType.Skeleton);
		this.m_entity = inEntity;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		((RemoteSkeletonEntity)this.m_entity).setMaxHealth(inMax);
	}

	@Override
	public int getMaxHealth()
	{
		return this.m_entity.getMaxHealth();
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
	}

	@Override
	public void loseTarget()
	{
	}
}
