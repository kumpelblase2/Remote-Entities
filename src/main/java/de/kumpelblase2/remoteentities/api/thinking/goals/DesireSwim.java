package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * Using this desire the entity will move upwards when its in water.
 */
public class DesireSwim extends DesireBase
{
	public DesireSwim(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.MOVEMENT_ADDITION;
		this.getNavigation().e(true);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getEntityHandle() != null && (this.getEntityHandle().G() || this.getEntityHandle().I());
	}

	@Override
	public boolean update()
	{
		if(this.getEntityHandle().aB().nextFloat() < 0.8F)
			NMSUtil.getControllerJump(this.getEntityHandle()).a();

		return true;
	}
}