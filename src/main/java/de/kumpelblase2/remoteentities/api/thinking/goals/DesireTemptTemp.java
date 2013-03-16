package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import net.minecraft.server.v1_5_R1.PathfinderGoalTempt;

public class DesireTemptTemp extends PathfinderGoalTempt
{
	private RemoteEntity m_entity;
	
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
