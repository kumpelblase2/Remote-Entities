package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.PathfinderGoalSit;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireSitTemp extends PathfinderGoalSit
{
	private final RemoteEntity m_entity;

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