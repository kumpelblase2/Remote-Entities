package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_6_R3.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * This desire searches for nearest entity which has this entity as a target and sets it as the target of this entity.
 */
public class DesireFindAttackingTarget extends DesireTargetBase
{
	@SerializeAs(pos = 4)
	protected boolean m_attackNearest;
	protected EntityLiving m_target;
	protected int m_lastAttackedTick;

	@Deprecated
	public DesireFindAttackingTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inAttackNearest)
	{
		super(inEntity, inDistance, inShouldCheckSight);
		this.m_attackNearest = inAttackNearest;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Deprecated
	public DesireFindAttackingTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee, boolean inAttackNearest)
	{
		super(inEntity, inDistance, inShouldCheckSight, inShouldMelee);
		this.m_attackNearest = inAttackNearest;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	public DesireFindAttackingTarget(float inDistance, boolean inShouldCheckSight, boolean inAttackNearest)
	{
		super(inDistance, inShouldCheckSight);
		this.m_attackNearest = inAttackNearest;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	public DesireFindAttackingTarget(float inDistance, boolean inShouldCheckSight, boolean inShouldMelee, boolean inAttackNearest)
	{
		super(inDistance, inShouldCheckSight, inShouldMelee);
		this.m_attackNearest = inAttackNearest;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		int lastAttackedTick = this.getEntityHandle().aF();

		return lastAttackedTick != this.m_lastAttackedTick && this.getEntityHandle() != null && this.isSuitableTarget(this.getEntityHandle().getLastDamager(), true);
	}

	@Override
	public boolean canContinue()
	{
		EntityLiving entityTarget = this.getEntityHandle().getLastDamager();
		return entityTarget != null && entityTarget != this.m_target;
	}

	@Override
	public void startExecuting()
	{
		EntityLiving entity = this.getEntityHandle();
		NMSUtil.setGoalTarget(entity, entity.getLastDamager());
		this.m_target = entity.getLastDamager();
		this.m_lastAttackedTick = this.getEntityHandle().aF();

		if(this.m_attackNearest)
		{
			@SuppressWarnings("unchecked")
			List<EntityLiving> list = entity.world.a(this.m_target.getClass(), AxisAlignedBB.a().a(entity.locX, entity.locY, entity.locZ, entity.locX + 1, entity.locY + 1, entity.locZ + 1).grow(this.m_distance, 4, this.m_distance));
			Iterator<EntityLiving> it = list.iterator();

			while(it.hasNext())
			{
				EntityLiving target = it.next();

				if(this.getEntityHandle() != target && NMSUtil.getGoalTarget(target) == null && !target.c(this.getEntityHandle().getLastDamager()))
					NMSUtil.setGoalTarget(target, entity.getLastDamager());
			}
		}
		super.startExecuting();
	}

	@Override
	public void stopExecuting()
	{
		if(NMSUtil.getGoalTarget(this.getEntityHandle()) != null && NMSUtil.getGoalTarget(this.getEntityHandle()) instanceof EntityHuman && ((EntityHuman)NMSUtil.getGoalTarget(this.getEntityHandle())).abilities.isInvulnerable)
			super.stopExecuting();
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}