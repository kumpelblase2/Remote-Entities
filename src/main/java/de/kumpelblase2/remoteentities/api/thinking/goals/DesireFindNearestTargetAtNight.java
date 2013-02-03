package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireFindNearestTargetAtNight extends DesireFindNearestTarget
{
	public DesireFindNearestTargetAtNight(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele, int inChance)
	{
		super(inEntity, inTargetClass, inDistance, inShouldCheckSight, inShouldMeele, inChance);
		this.m_onlyAtNight = true;
	}
	
	public DesireFindNearestTargetAtNight(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, false, inChance);
	}
	
	public DesireFindNearestTargetAtNight(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inMelee, Class<? extends EntityLiving> inTargetClass, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, inMelee, inChance);
	}
}
