package de.kumpelblase2.remoteentities.api.thinking.selectors;

import net.minecraft.server.v1_5_R3.Entity;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.IEntitySelector;

public class EntitySelectorViewable implements IEntitySelector
{
	private final EntityLiving m_entity;
	
	public EntitySelectorViewable(EntityLiving inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public boolean a(Entity inEntity)
	{
		return this.m_entity.getEntitySenses().canSee(inEntity);
	}
}