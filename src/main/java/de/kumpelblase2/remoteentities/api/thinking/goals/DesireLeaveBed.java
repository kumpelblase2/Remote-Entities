package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.entities.RemotePlayer;

public class DesireLeaveBed extends DesireBase
{
	public DesireLeaveBed(RemotePlayer inEntity)
	{
		super(inEntity);
		this.m_isContinuous = false;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getEntityHandle().world.v() && ((RemotePlayer)this.m_entity).isSleeping();
	}
	
	@Override
	public void startExecuting()
	{
		((RemotePlayer)this.m_entity).leaveBed();
	}
}
