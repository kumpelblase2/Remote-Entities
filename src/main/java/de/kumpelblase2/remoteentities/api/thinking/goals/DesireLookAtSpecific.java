package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

/**
 * Using this desire the entity will try and look at the given entity when it's near.
 */
public class DesireLookAtSpecific extends DesireLookAtNearest
{
	private final EntityLiving m_specificTarget;

	public DesireLookAtSpecific(RemoteEntity inEntity, EntityLiving inTarget, float inMinDistance)
	{
		super(inEntity, inTarget.getClass(), inMinDistance);
		this.m_specificTarget = inTarget;
	}

	public DesireLookAtSpecific(RemoteEntity inEntity, LivingEntity inTarget, float inMinDistance)
	{
		this(inEntity, ((CraftLivingEntity)inTarget).getHandle(), inMinDistance);
	}

	@Deprecated
	public DesireLookAtSpecific(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance, float inPossibility, EntityLiving inTargetEntity)
	{
		this(inEntity, inTargetEntity, inMinDistance);
		this.m_lookPossibility = inPossibility;
	}

	@Override
	public boolean shouldExecute()
	{
		if(!super.shouldExecute())
			return false;

		if(this.m_target != this.m_specificTarget)
			this.m_target = this.m_specificTarget;

		return true;
	}
}