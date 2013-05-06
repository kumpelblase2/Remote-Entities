package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import net.minecraft.server.v1_5_R3.PathfinderGoalPassengerCarrotStick;

public class DesireFollowCarrotStickTemp extends PathfinderGoalPassengerCarrotStick
{
	protected final RemoteEntity m_entity;
	
	public DesireFollowCarrotStickTemp(RemoteEntity inEntity)
	{
		super(null, 0);
		this.m_entity = inEntity;
	}
	
	public boolean f()
	{
		if(this.m_entity.getMind().getMovementDesire(DesireFollowCarrotStick.class) == null)
			return false;

		return this.m_entity.getMind().getMovementDesire(DesireFollowCarrotStick.class).isSpeedBoosted();
	}
	
	public void g()
	{
		if(this.m_entity.getMind().getMovementDesire(DesireFollowCarrotStick.class) == null)
			return;
		
		this.m_entity.getMind().getMovementDesire(DesireFollowCarrotStick.class).boostSpeed();
	}
	
	public boolean h()
	{
		if(this.m_entity.getMind().getMovementDesire(DesireFollowCarrotStick.class) == null)
			return false;
		
		return this.m_entity.getMind().getMovementDesire(DesireFollowCarrotStick.class).isControlledByPlayer();
	}
}
