package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;

public class DesireSwim extends DesireBase
{
	public DesireSwim(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.MOVEMENT_ADDITION;
		this.getEntityHandle().getNavigation().e(true);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getEntityHandle() != null && (this.getEntityHandle().G() || this.getEntityHandle().I());
	}
	
	@Override
	public boolean update()
	{
		if(this.getEntityHandle().aE().nextFloat() < 0.8F)
			this.getEntityHandle().getControllerJump().a();
		
		return true;
	}
}
