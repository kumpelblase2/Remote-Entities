package de.kumpelblase2.remoteentities.api.thinking;

import net.minecraft.server.v1_6_R2.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public abstract class RideBehavior extends BaseBehavior
{
	@SerializeAs(pos = 1)
	private boolean m_jumpEnabled;

	public RideBehavior(RemoteEntity inEntity)
	{
		this(inEntity, true);
	}

	public RideBehavior(RemoteEntity inEntity, boolean inJumpEnabled)
	{
		super(inEntity);
		this.m_name = "Ride";
		this.m_jumpEnabled = inJumpEnabled;
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
		entity.Y = 0.5f;
		if(inMotion[0] == 0)
			inMotion[0] = ((EntityLiving)entity.passenger).be * 0.5f;

		if(inMotion[1] == 0)
			inMotion[1] = ((EntityLiving)entity.passenger).bf;

		if(this.m_jumpEnabled && ReflectionUtil.isJumping((EntityLiving)entity.passenger))
		{
			if(entity.onGround)
				inMotion[2] = 0.5f;
		}

		this.applyMotionChanges(inMotion);
	}

	/**
	 *
	 *
	 * @param inMotion
	 */
	protected void applyMotionChanges(float[] inMotion)
	{
		if(inMotion[1] > 0)
			inMotion[1] *= 1.2;
		else
			inMotion[1] *= 0.75;
	}

	public boolean isJumpEnabled()
	{
		return this.m_jumpEnabled;
	}

	public void setJumpEnabled(boolean inEnabled)
	{
		this.m_jumpEnabled = inEnabled;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}