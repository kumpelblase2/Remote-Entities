package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_5.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireInteract extends DesireLookAtNearest
{
	public DesireInteract(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inMinDistance)
	{
		super(inEntity, inTarget, inMinDistance);
		this.m_type = 3;
	}
}
