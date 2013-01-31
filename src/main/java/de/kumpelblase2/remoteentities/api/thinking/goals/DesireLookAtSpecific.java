package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.v1_4_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import net.minecraft.server.v1_4_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireLookAtSpecific extends DesireLookAtNearest
{
	private final EntityLiving m_specificTarget;
	
	public DesireLookAtSpecific(RemoteEntity inEntity, EntityLiving inTarget, float inDelay)
	{
		super(inEntity, inTarget.getClass(), inDelay);
		this.m_specificTarget = inTarget;
	}
	
	public DesireLookAtSpecific(RemoteEntity inEntity, LivingEntity inTarget, float inDelay)
	{
		this(inEntity, ((CraftLivingEntity)inTarget).getHandle(), inDelay);
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
