package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_6.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireAttackNearestAtNight extends DesireAttackNearest
{

	public DesireAttackNearestAtNight(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele, int inChance)
	{
		super(inEntity, inTargetClass, inDistance, inShouldCheckSight, inShouldMeele, inChance);
		this.m_onlyAtNight = true;
	}
}
