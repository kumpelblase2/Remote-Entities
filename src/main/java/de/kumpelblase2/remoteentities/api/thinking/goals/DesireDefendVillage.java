package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Village;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

public class DesireDefendVillage extends DesireTargetBase
{
	protected EntityLiving m_nextTarget;
	
	public DesireDefendVillage(RemoteEntity inEntity)
	{
		this(inEntity, 16, false, true);
	}
	
	public DesireDefendVillage(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele)
	{
		super(inEntity, inDistance, inShouldCheckSight, inShouldMeele);
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		Village nextVillage;
		if(this.getRemoteEntity().getHandle() instanceof EntityIronGolem)
			nextVillage = ((EntityIronGolem)this.getRemoteEntity().getHandle()).n();
		else
			nextVillage = WorldUtilities.getClosestVillage(this.getRemoteEntity().getHandle());
		
		if(nextVillage == null)
			return false;
		else
		{
			this.m_nextTarget = nextVillage.b(this.getRemoteEntity().getHandle());
			return this.isSuitableTarget(this.m_nextTarget, false);
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().b(this.m_nextTarget);
		super.startExecuting();
	}
}
