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
		return this.getEntityHandle() != null && this.getEntityHandle().world.t();
	}
	
	@Override
	public void startExecuting()
	{
		this.getEntityHandle().getNavigation().d(true);
	}
	
	@Override
	public void stopExecuting()
	{
		this.getEntityHandle().getNavigation().d(false);
	}
}
