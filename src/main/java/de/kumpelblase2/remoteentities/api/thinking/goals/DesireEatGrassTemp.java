package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import net.minecraft.server.v1_5_R2.PathfinderGoalEatTile;

public class DesireEatGrassTemp extends PathfinderGoalEatTile
{
	private final RemoteEntity m_entity;
	
	public DesireEatGrassTemp(RemoteEntity inEntity)
	{
		super(null);
		this.m_entity = inEntity;
	}
	
	@Override
	public int f()
	{
		if(this.m_entity.getMind().getMovementDesire(DesireEatGrass.class) == null)
			return 0;
		
		return this.m_entity.getMind().getMovementDesire(DesireEatGrass.class).tickTime();
	}
}
