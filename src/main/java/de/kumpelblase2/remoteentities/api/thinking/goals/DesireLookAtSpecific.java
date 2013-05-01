package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import net.minecraft.server.v1_5_R3.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

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
