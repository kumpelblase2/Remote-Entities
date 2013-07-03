package de.kumpelblase2.remoteentities.api.thinking.selectors;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.IEntitySelector;

public class EntitySelectorLiving implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity.isAlive();
	}
}