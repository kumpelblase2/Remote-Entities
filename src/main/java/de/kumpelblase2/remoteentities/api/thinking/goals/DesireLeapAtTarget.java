package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireLeapAtTarget extends DesireBase
{
	protected float m_yMotion;
	protected EntityLiving m_target;
	
	public DesireLeapAtTarget(RemoteEntity inEntity, float inYMotion)
	{
		super(inEntity);
		this.m_yMotion = inYMotion;
		this.m_type = 5;
	}

	@Override
	public boolean shouldExecute()
	{
		this.m_target = this.getRemoteEntity().getHandle().az();
		if(this.m_target == null)
			return false;
		else
		{
			double dist = this.getRemoteEntity().getHandle().e(this.m_target);
			return dist >= 4 && dist <= 16 ? (!this.getRemoteEntity().getHandle().onGround ? false : this.getRemoteEntity().getHandle().au().nextInt(5) == 0) : false;
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.getRemoteEntity().getHandle().onGround;
	}
	
	@Override
	public boolean update()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		double xDiff = this.m_target.locX - entity.locX;
		double zDiff = this.m_target.locZ - entity.locZ;
		float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
		
		entity.motX = xDiff / dist * 0.5D * 0.800000011920929D + entity.motX * 0.20000000298023224D;
		entity.motZ = zDiff / dist * 0.5D * 0.800000011920929D + entity.motZ * 0.20000000298023224D;
		entity.motY = this.m_yMotion;
		return true;
	}
}
