package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;

public class DesireMoveToTarget extends DesireBase
{
	protected float m_minDistance;
	protected EntityLiving m_target;
	protected double m_x;
	protected double m_y;
	protected double m_z;
	
	public DesireMoveToTarget(RemoteEntity inEntity, float inMinDistance)
	{
		super(inEntity);
		this.m_minDistance = inMinDistance;
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		this.m_target = this.getRemoteEntity().getHandle().az();
		if(this.m_target == null)
			return false;
		else if(this.m_target.e(this.getRemoteEntity().getHandle()) > this.m_minDistance * this.m_minDistance)
			return false;
		else
		{
			Vec3D vec = RandomPositionGenerator.a(this.getRemoteEntity().getHandle(), 16, 7, Vec3D.a().create(this.m_target.locX, this.m_target.locY, this.m_target.locZ));
			
			if(vec == null)
				return false;
			else
			{
				this.m_x = vec.a;
				this.m_y = vec.b;
				this.m_z = vec.c;
				return true;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.getRemoteEntity().getHandle().getNavigation().f() && this.m_target.isAlive() && this.m_target.e(this.getRemoteEntity().getHandle()) < this.m_minDistance * this.m_minDistance;
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().a(this.m_x, this.m_y, this.m_z, this.getRemoteEntity().getSpeed());
	}
}
