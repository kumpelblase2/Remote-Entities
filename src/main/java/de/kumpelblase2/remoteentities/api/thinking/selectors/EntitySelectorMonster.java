package de.kumpelblase2.remoteentities.api.thinking.selectors;

import net.minecraft.server.v1_7_R3.*;

public class EntitySelectorMonster implements IEntitySelector
{
	@Override
	public boolean a(Entity inEntity)
	{
		return inEntity instanceof IMonster;
	}
}