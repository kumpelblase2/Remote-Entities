package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R3.EntityCreeper;
import net.minecraft.server.v1_5_R3.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.exceptions.NotACreeperException;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the creeper will start to explode when it's near its target.
 */
public class DesireSwell extends DesireBase
{
	protected EntityCreeper m_creeper;
	protected EntityLiving m_target;
	@SerializeAs(pos = 1)
	protected int m_minDistance;
	protected int m_minDistanceSqr;
	@SerializeAs(pos = 2)
	protected int m_maxDistance;
	protected int m_maxDistanceSqr;

	public DesireSwell(RemoteEntity inEntity)
	{
		this(inEntity, 3, 7);
	}

	public DesireSwell(RemoteEntity inEntity, int inMinDistance, int inMaxDistance)
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityCreeper))
			throw new NotACreeperException();

		this.m_creeper = (EntityCreeper)this.getEntityHandle();
		this.m_type = DesireType.PRIMAL_INSTINCT;
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSqr = inMinDistance * inMinDistance;
		this.m_maxDistance = inMaxDistance;
		this.m_maxDistanceSqr = inMaxDistance * inMaxDistance;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_creeper == null)
			return false;

		EntityLiving target = this.m_creeper.getGoalTarget();
		return this.m_creeper.o() > 0 || target != null && this.m_creeper.e(target) < this.m_minDistanceSqr;
	}

	@Override
	public void startExecuting()
	{
		this.m_creeper.getNavigation().g();
		this.m_target = this.m_creeper.getGoalTarget();
	}

	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}

	@Override
	public boolean update()
	{
		if(this.m_target == null)
			this.m_creeper.a(-1);
		else if(this.m_creeper.e(this.m_target) > this.m_maxDistanceSqr)
			this.m_creeper.a(-1);
		else if(!this.m_creeper.getEntitySenses().canSee(this.m_target))
			this.m_creeper.a(-1);
		else
			this.m_creeper.a(1);
		return true;
	}

	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}