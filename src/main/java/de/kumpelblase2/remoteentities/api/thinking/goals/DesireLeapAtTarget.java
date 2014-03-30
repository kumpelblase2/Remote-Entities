package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will leap towards the target once it comes into a certain range of the entity.
 */
public class DesireLeapAtTarget extends DesireBase
{
	@SerializeAs(pos = 1)
	protected float m_yMotion;
	protected EntityLiving m_target;

	@Deprecated
	public DesireLeapAtTarget(RemoteEntity inEntity, float inYMotion)
	{
		super(inEntity);
		this.m_yMotion = inYMotion;
		this.m_type = DesireType.OCCASIONAL_URGE;
	}

	public DesireLeapAtTarget(float inYMotion)
	{
		super();
		this.m_yMotion = inYMotion;
		this.m_type = DesireType.OCCASIONAL_URGE;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		this.m_target = NMSUtil.getGoalTarget(this.getEntityHandle());
		if(this.m_target == null)
			return false;
		else
		{
			double dist = this.getEntityHandle().e(this.m_target);
			return dist >= 4 && dist <= 16 && this.getEntityHandle().onGround && this.getEntityHandle().aH().nextInt(5) == 0;
		}
	}

	@Override
	public boolean canContinue()
	{
		return !this.getEntityHandle().onGround;
	}

	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		double xDiff = this.m_target.locX - entity.locX;
		double zDiff = this.m_target.locZ - entity.locZ;
		float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);

		entity.motX = xDiff / dist * 0.5D * 0.800000011920929D + entity.motX * 0.20000000298023224D;
		entity.motZ = zDiff / dist * 0.5D * 0.800000011920929D + entity.motZ * 0.20000000298023224D;
		entity.motY = this.m_yMotion;
		return true;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}