package de.kumpelblase2.remoteentities.api.thinking.selectors;

import net.minecraft.server.v1_6_R3.*;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

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
		return NMSUtil.getEntitySenses(this.m_entity).canSee(inEntity);
	}
}