package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import net.minecraft.server.*;

public class DesireLookAtNearest extends DesireBase
{
	protected EntityLiving m_target;
	protected Class<? extends EntityLiving> m_toLookAt;
	protected int m_lookTicks;
	protected float m_minDist;
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
		this.m_lookPossibility = inPossibility;
	}

	@Override
	public void startExecuting()
	{
		this.m_lookTicks = 40 + this.getRemoteEntity().getHandle().au().nextInt(40);
	}

	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}

	@Override
	public boolean update()
	{
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_target.locX, this.m_target.locY + this.m_target.getHeadHeight(), this.m_target.locZ, 10, this.getRemoteEntity().getHandle().bf());
		this.m_lookTicks--;
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		if(entity.au().nextFloat() >= this.m_lookPossibility)
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
		return !this.m_target.isAlive() ? false : (this.getRemoteEntity().getHandle().e(this.m_target) > (this.m_minDist * this.m_minDist) ? false : this.m_lookTicks > 0);
	}
}
