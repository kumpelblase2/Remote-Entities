package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.List;
import net.minecraft.server.v1_6_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.*;

/**
 * Using this desire the entity will try to look at the nearest entity of the given type.
 */
public class DesireLookAtNearest extends DesireBase
{
	protected EntityLiving m_target;
	@SerializeAs(pos = 1)
	protected Class<? extends Entity> m_toLookAt;
	protected int m_lookTicks;
	@SerializeAs(pos = 2)
	protected float m_minDist;
	protected float m_minDistSquared;
	@SerializeAs(pos = 3)
	protected float m_lookPossibility;

	public DesireLookAtNearest(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance)
	{
		this(inEntity, inTarget, inMinDistance, 0.02F);
	}

	@SuppressWarnings("unchecked")
	public DesireLookAtNearest(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance, float inPossibility)
	{
		super(inEntity);
		if(Entity.class.isAssignableFrom(inTarget))
			this.m_toLookAt = (Class<? extends Entity>)inTarget;
		else
			this.m_toLookAt = (Class<? extends Entity>)NMSClassMap.getNMSClass(inTarget);
		this.m_minDist = inMinDistance;
		this.m_minDistSquared = this.m_minDist * this.m_minDist;
		this.m_lookPossibility = inPossibility;
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	@Override
	public void startExecuting()
	{
		this.m_lookTicks = 40 + this.getEntityHandle().aB().nextInt(40);
	}

	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}

	@Override
	public boolean update()
	{
		NMSUtil.getControllerLook(this.getEntityHandle()).a(this.m_target.locX, this.m_target.locY + this.m_target.getHeadHeight(), this.m_target.locZ, 10, NMSUtil.getMaxHeadRotation(this.getEntityHandle()));
		this.m_lookTicks--;
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null)
			return false;

		if(entity.aB().nextFloat() >= this.m_lookPossibility)
			return false;
		else
		{
			if(this.m_toLookAt == EntityHuman.class || this.m_toLookAt == EntityPlayer.class)
				this.m_target = entity.world.findNearbyPlayer(entity, this.m_minDist);
			else
				this.m_target = (EntityLiving)entity.world.a(this.m_toLookAt, entity.boundingBox.grow(this.m_minDist, 3, this.m_minDist), entity);

			return this.m_target != null;
		}
	}

	@Override
	public boolean canContinue()
	{
		if(!this.m_target.isAlive())
			return false;

		return this.getEntityHandle().e(this.m_target) <= this.m_minDistSquared && this.m_lookTicks > 0;
	}

	@Override
	public ParameterData[] getSerializeableData()
	{
		List<ParameterData> thisData = ReflectionUtil.getParameterDataForClass(this);
		return thisData.toArray(new ParameterData[thisData.size()]);
	}
}