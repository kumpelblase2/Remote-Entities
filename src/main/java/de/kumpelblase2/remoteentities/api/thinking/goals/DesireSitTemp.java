package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import net.minecraft.server.v1_4_R1.PathfinderGoalSit;

public class DesireSitTemp extends PathfinderGoalSit
{
	private RemoteEntity m_entity;
	
	public DesireSitTemp(RemoteEntity arg0)
	{
		super(null);
		this.m_entity = arg0;
	}
	
	@Override
	public void setSitting(boolean flag)
	{
		if(this.m_entity.getMind().getMovementDesire(DesireSit.class) == null)
			return;
		
		this.m_entity.getMind().getMovementDesire(DesireSit.class).canSit(flag);
	}
}
