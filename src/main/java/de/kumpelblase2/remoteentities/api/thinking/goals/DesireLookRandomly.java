package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * Using this desire the entity will occasionally look around randomly.
 */
public class DesireLookRandomly extends DesireBase
{
	protected double m_xDiff;
	protected double m_zDiff;
	protected int m_lookTick;

	public DesireLookRandomly(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		return this.getEntityHandle().aB().nextFloat() < 0.02F;
	}

	@Override
	public boolean canContinue()
	{
		return this.m_lookTick >= 0;
	}

	@Override
	public void startExecuting()
	{
		double d = 6.283185307179586D * this.getEntityHandle().aB().nextDouble();

		this.m_xDiff = Math.cos(d);
		this.m_zDiff = Math.sin(d);
		this.m_lookTick = 20 + this.getEntityHandle().aB().nextInt(20);
	}

	@Override
	public boolean update()
	{
		this.m_lookTick--;
		EntityLiving entity = this.getEntityHandle();
		NMSUtil.getControllerLook(entity).a(entity.locX + this.m_xDiff, entity.locY + entity.getHeadHeight(), entity.locZ + this.m_zDiff, 10, NMSUtil.getMaxHeadRotation(entity));
		return true;
	}
}