package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.entities.RemotePlayer;

/**
 * Using this desire the player will leave the bed once the sun comes out.
 */
public class DesireLeaveBed extends DesireBase
{
	@Deprecated
	public DesireLeaveBed(RemotePlayer inEntity)
	{
		super(inEntity);
		this.m_isContinuous = false;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	public DesireLeaveBed()
	{
		super();
		this.m_isContinuous = false;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getEntityHandle().world.w() && ((RemotePlayer)this.m_entity).isSleeping();
	}

	@Override
	public void startExecuting()
	{
		((RemotePlayer)this.m_entity).leaveBed();
	}
}