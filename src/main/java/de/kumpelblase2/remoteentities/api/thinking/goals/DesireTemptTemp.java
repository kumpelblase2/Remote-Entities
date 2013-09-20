package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R3.PathfinderGoalTempt;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireTemptTemp extends PathfinderGoalTempt
{
	private final RemoteEntity m_entity;
	
	public DesireTemptTemp(RemoteEntity inEntity)
	{
		super(null, 0, 1, false);
		this.m_entity = inEntity;
	}
	
	@Override
	public boolean f()
	{
		if(this.m_entity.getMind().getMovementDesire(DesireTempt.class) == null)
			return false;
		
		return this.m_entity.getMind().getMovementDesire(DesireTempt.class).isTempted();
	}
}
