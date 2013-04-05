package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.IEntitySelector;
import net.minecraft.server.v1_5_R2.IMonster;

public class EntitySelectorMonster implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity instanceof IMonster;
	}
}
