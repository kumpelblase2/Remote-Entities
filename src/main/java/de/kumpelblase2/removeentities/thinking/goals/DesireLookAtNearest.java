package de.kumpelblase2.removeentities.thinking.goals;

import de.kumpelblase2.removeentities.entities.RemoteEntity;
import de.kumpelblase2.removeentities.entities.RemoteEntityType;
import de.kumpelblase2.removeentities.thinking.Desire;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PathfinderGoalLookAtPlayer;

public class DesireLookAtNearest extends PathfinderGoalLookAtPlayer implements Desire
{
	private final RemoteEntity m_entity;
	
	public DesireLookAtNearest(RemoteEntity inEntity, RemoteEntityType inTarget, float inDelay)
	{
		this(inEntity, inTarget.getEntityClass(), inDelay);
	}
	
	public DesireLookAtNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inDelay)
	{
		super(inEntity.getHandle(), inTarget, inDelay);
		this.m_entity = inEntity;
	}
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
	
	@Override
	public void d()
	{
		super.d();
		if(this.m_entity.getHandle() instanceof EntityPlayer)
		{
			this.m_entity.getHandle().yaw = this.m_entity.getHandle().as;
		}
	}
}
