package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireRestrictSun extends DesireBase
{	
	public DesireRestrictSun(RemoteEntity inEntity)
	{
		super(inEntity);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getRemoteEntity().getHandle().world.t();
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().d(true);
	}
	
	@Override
	public void stopExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().d(false);
	}
}
