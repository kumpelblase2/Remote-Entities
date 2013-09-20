package de.kumpelblase2.remoteentities.api.thinking.selectors;

import net.minecraft.server.v1_6_R3.*;

public class EntitySelectorNotUndead implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity instanceof EntityLiving && ((EntityLiving)inEntity).getMonsterType() != EnumMonsterType.UNDEAD;
	}
}