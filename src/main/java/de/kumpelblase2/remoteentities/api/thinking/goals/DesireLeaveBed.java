package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.entities.RemotePlayer;

public class DesireLeaveBed extends DesireBase
{
	public DesireLeaveBed(RemotePlayer inEntity)
	{
		super(inEntity);
		this.m_isContinous = false;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle().world.u() && ((RemotePlayer)this.m_entity).isSleeping())
			return true;
		
		return false;
	}
	
	@Override
	public void startExecuting()
	{
		((RemotePlayer)this.m_entity).leaveBed();
	}
}
