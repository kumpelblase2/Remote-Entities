package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R2.EntityIronGolem;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.Village;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

public class DesireDefendVillage extends DesireTargetBase
{
	protected EntityLiving m_nextTarget;
	
	public DesireDefendVillage(RemoteEntity inEntity)
	{
		this(inEntity, 16f, false, true);
	}
	
	public DesireDefendVillage(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele)
	{
		super(inEntity, inDistance, inShouldCheckSight, inShouldMeele);
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		Village nextVillage;
		if(this.getEntityHandle() instanceof EntityIronGolem)
			nextVillage = ((EntityIronGolem)this.getEntityHandle()).m();
		else
			nextVillage = WorldUtilities.getClosestVillage(this.getEntityHandle());
		
		if(nextVillage == null)
			return false;
		else
		{
			this.m_nextTarget = nextVillage.b(this.getEntityHandle());
			if(!this.isSuitableTarget(this.m_nextTarget, false))
			{
				if(this.getEntityHandle().aE().nextInt(20) == 0)
				{
					this.m_nextTarget = nextVillage.c(this.getEntityHandle());
					return this.isSuitableTarget(this.m_nextTarget, false);
				}
				else
					return false;
			}
			return true;
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.getEntityHandle().setGoalTarget(this.m_nextTarget);
		super.startExecuting();
	}
}
