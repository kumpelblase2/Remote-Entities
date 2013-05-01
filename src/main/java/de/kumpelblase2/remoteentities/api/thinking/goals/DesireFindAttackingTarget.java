package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;
import net.minecraft.server.v1_5_R3.*;
import java.util.Iterator;
import java.util.List;

public class DesireFindAttackingTarget extends DesireTargetBase
{
	@SerializeAs(pos = 4)
	protected boolean m_attackNearest;
	protected EntityLiving m_target;
	
	
	public DesireFindAttackingTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inAttackNearest)
	{
		super(inEntity, inDistance, inShouldCheckSight);
		this.m_attackNearest = inAttackNearest;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}
	
	public DesireFindAttackingTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee, boolean inAttackNearest)
	{
		super(inEntity, inDistance, inShouldCheckSight, inShouldMelee);
		this.m_attackNearest = inAttackNearest;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getEntityHandle() != null && this.isSuitableTarget(this.getEntityHandle().aF(), true);
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving entityTarget = this.getEntityHandle().aF();
		return entityTarget != null && entityTarget != this.m_target; 
	}
	
	@Override
	public void startExecuting()
	{
		EntityLiving entity = this.getEntityHandle();
		entity.setGoalTarget(entity.aF());
		this.m_target = entity.aF();
		
		if(this.m_attackNearest)
		{
			@SuppressWarnings("unchecked")
			List<EntityLiving> list = entity.world.a(this.m_target.getClass(), AxisAlignedBB.a().a(entity.locX, entity.locY, entity.locZ, entity.locX + 1, entity.locY + 1, entity.locZ + 1).grow(this.m_distance, 4, this.m_distance));
			Iterator<EntityLiving> it = list.iterator();
			
			while(it.hasNext())
			{
				EntityLiving target = it.next();
				
				if(this.getEntityHandle() != target && target.getGoalTarget() == null)
					target.setGoalTarget(entity.aF());
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
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
