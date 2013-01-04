package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import net.minecraft.server.v1_4_6.*;

public class DesireLookAtNearest extends DesireBase
{
	protected EntityLiving m_target;
	protected Class<? extends EntityLiving> m_toLookAt;
	protected int m_lookTicks;
	protected float m_minDist;
	protected float m_minDistSquared;
	protected float m_lookPossibility;
	
	public DesireLookAtNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inMinDistance)
	{
		this(inEntity, inTarget, inMinDistance, 0.02F);
	}
	
	public DesireLookAtNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inMinDistance, float inPossibility)
	{
		super(inEntity);
		this.m_toLookAt = inTarget;
		this.m_minDist = inMinDistance;
		this.m_minDistSquared = this.m_minDist * this.m_minDist;
		this.m_lookPossibility = inPossibility;
	}

	@Override
	public void startExecuting()
	{
		this.m_lookTicks = 40 + this.getEntityHandle().aB().nextInt(40);
	}

	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}

	@Override
	public boolean update()
	{
		this.getEntityHandle().getControllerLook().a(this.m_target, 10, this.getEntityHandle().bp());
		this.m_lookTicks--;
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null)
			return false;
		
		if(entity.aB().nextFloat() >= this.m_lookPossibility)
			return false;
		else
		{
			if(this.m_toLookAt == EntityHuman.class || this.m_toLookAt == EntityPlayer.class)
				this.m_target = entity.world.findNearbyPlayer(entity, this.m_minDist);
			else
				this.m_target = (EntityLiving)entity.world.a(this.m_toLookAt, entity.boundingBox.grow(this.m_minDist, 3, this.m_minDist), entity);
			
			return this.m_target != null;
		}
	}

	@Override
	public boolean canContinue()
	{
		return !this.m_target.isAlive() ? false : (this.getEntityHandle().e(this.m_target) > this.m_minDistSquared ? false : this.m_lookTicks > 0);
	}
}
