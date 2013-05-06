package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_5_R3.Entity;
import net.minecraft.server.v1_5_R3.IEntitySelector;

public class EntitySelectorLiving implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity.isAlive();
	}
}
