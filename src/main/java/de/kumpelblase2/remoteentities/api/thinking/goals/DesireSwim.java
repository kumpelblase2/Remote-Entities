package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireSwim extends DesireBase
{
	public DesireSwim(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 4;
		this.getRemoteEntity().getHandle().getNavigation().e(true);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getRemoteEntity().getHandle().H() || this.getRemoteEntity().getHandle().J();
	}
	
	@Override
	public boolean update()
	{
		if(this.getRemoteEntity().getHandle().au().nextFloat() < 0.8F)
			this.getRemoteEntity().getHandle().getControllerJump().a();
		return true;
	}
}
