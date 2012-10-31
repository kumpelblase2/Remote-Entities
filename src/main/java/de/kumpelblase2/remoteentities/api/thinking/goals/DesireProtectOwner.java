package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireProtectOwner extends DesireTargetBase
{
	protected EntityTameableAnimal m_animal;
	protected EntityLiving m_ownerTarget;
	
	public DesireProtectOwner(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight) throws Exception
	{
		super(inEntity, inDistance, inShouldCheckSight);
		if(!(this.getRemoteEntity().getHandle() instanceof EntityTameableAnimal))
			throw new Exception("Entity needs owner");
		
		this.m_animal = (EntityTameableAnimal)this.getRemoteEntity().getHandle();
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(!this.m_animal.isTamed())
			return false;
		else
		{
			EntityLiving owner = this.m_animal.getOwner();
			if(owner == null)
				return false;
			else
			{
				this.m_ownerTarget = owner.av();
				return this.isSuitableTarget(this.m_ownerTarget, false);
			}
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().b(this.m_ownerTarget);
		super.startExecuting();
	}
}
