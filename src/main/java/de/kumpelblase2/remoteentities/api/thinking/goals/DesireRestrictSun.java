package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * Using this desire the entity will try to choose a dark path when moving at daytime.
 */
public class DesireRestrictSun extends DesireBase
{
	public DesireRestrictSun(RemoteEntity inEntity)
	{
		super(inEntity);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getEntityHandle() != null && this.getEntityHandle().world.v();
	}

	@Override
	public void startExecuting()
	{
		NMSUtil.getNavigation(this.getEntityHandle()).d(true);
	}

	@Override
	public void stopExecuting()
	{
		NMSUtil.getNavigation(this.getEntityHandle()).d(false);
	}
}