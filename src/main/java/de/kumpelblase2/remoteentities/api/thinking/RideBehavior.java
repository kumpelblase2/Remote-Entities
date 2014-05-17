package de.kumpelblase2.remoteentities.api.thinking;

import net.minecraft.server.v1_7_R3.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public abstract class RideBehavior extends BaseBehavior
{
	@SerializeAs(pos = 1)
	private boolean m_jumpEnabled;
	@SerializeAs(pos = 2)
	private boolean m_canFly;

	public RideBehavior(RemoteEntity inEntity)
	{
		this(inEntity, true);
	}

	public RideBehavior(RemoteEntity inEntity, boolean inJumpEnabled)
	{
		this(inEntity, inJumpEnabled, false);
	}

	public RideBehavior(RemoteEntity inEntity, boolean inJumpEnabled, boolean inCanFly)
	{
		super(inEntity);
		this.m_name = "Ride";
		this.m_jumpEnabled = inJumpEnabled;
		this.m_canFly = inCanFly;
	}

	/**
	 *
	 *
	 * @param inMotion
	 */
	public void ride(float[] inMotion)
	{
		EntityLiving entity = this.m_entity.getHandle();
		if(entity.passenger == null)
			return;

		this.m_entity.setYaw(entity.passenger.yaw);
		this.m_entity.setPitch(entity.passenger.pitch);
		entity.W = 0.5f;
		if(inMotion[0] == 0)
			inMotion[0] = ((EntityLiving)entity.passenger).bd * 0.5f;

		if(inMotion[1] == 0)
			inMotion[1] = ((EntityLiving)entity.passenger).be;

		if(this.m_canFly)
		{
			if(entity.passenger instanceof EntityLiving)
			{
				if(ReflectionUtil.isJumping(entity.passenger))
					inMotion[2] = 0.5f;
				else if(((EntityLiving)entity.passenger).pitch >= 40)
					inMotion[2] = -0.15f;
			}
		}
		else if(this.m_jumpEnabled && ReflectionUtil.isJumping(entity.passenger))
		{
			if(entity.onGround)
				inMotion[2] = 0.5f;
		}

		this.onRide(inMotion);
	}

	/**
	 *
	 *
	 * @param inMotion
	 */
	protected abstract void onRide(float[] inMotion);

	public boolean isJumpEnabled()
	{
		return this.m_jumpEnabled;
	}

	public void setJumpEnabled(boolean inEnabled)
	{
		this.m_jumpEnabled = inEnabled;
	}

	public boolean canFly()
	{
		return this.m_canFly;
	}

	public void setCanFly(boolean inFly)
	{
		this.m_canFly = inFly;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}