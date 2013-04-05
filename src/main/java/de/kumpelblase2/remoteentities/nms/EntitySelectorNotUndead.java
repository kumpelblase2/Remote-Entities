package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.EnumMonsterType;
import net.minecraft.server.v1_5_R2.IEntitySelector;

public class EntitySelectorNotUndead implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity instanceof EntityLiving && ((EntityLiving)inEntity).getMonsterType() != EnumMonsterType.UNDEAD;
	}
}
