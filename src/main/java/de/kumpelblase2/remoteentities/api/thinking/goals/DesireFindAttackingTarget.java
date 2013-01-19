package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_4_R1.AxisAlignedBB;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireFindAttackingTarget extends DesireTargetBase
{
	protected boolean m_attackNearest;
	protected EntityLiving m_target;
	
	
	public DesireFindAttackingTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inAttackNearest)
	{
		super(inEntity, inDistance, inShouldCheckSight);
		this.m_attackNearest = inAttackNearest;
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		return this.isSuitableTarget(this.getEntityHandle().aC(), true);
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving entityTarget = this.getEntityHandle().aC();
		return entityTarget != null && entityTarget != this.m_target; 
	}
	
	@Override
	public void startExecuting()
	{
		EntityLiving entity = this.getEntityHandle();
		entity.setGoalTarget(entity.aC());
		this.m_target = entity.aC();
		
		if(this.m_attackNearest)
		{
			@SuppressWarnings("unchecked")
			List<EntityLiving> list = entity.world.a(this.m_target.getClass(), AxisAlignedBB.a().a(entity.locX, entity.locY, entity.locZ, entity.locX + 1, entity.locY + 1, entity.locZ + 1).grow(this.m_distance, 4, this.m_distance));
			Iterator<EntityLiving> it = list.iterator();
			
			while(it.hasNext())
			{
				EntityLiving target = it.next();
				
				if(this.getEntityHandle() != target && target.getGoalTarget() == null)
					target.setGoalTarget(entity.aC());
			}
		}
		super.startExecuting();
	}
	
	@Override
	public void stopExecuting()
	{
		if(this.getEntityHandle().getGoalTarget() != null && this.getEntityHandle().getGoalTarget() instanceof EntityHuman && ((EntityHuman)this.getEntityHandle().getGoalTarget()).abilities.isInvulnerable)
			super.stopExecuting();
	}
}
