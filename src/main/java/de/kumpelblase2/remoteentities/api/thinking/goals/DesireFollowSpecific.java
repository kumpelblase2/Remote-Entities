package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.entity.LivingEntity;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireFollowSpecific extends DesireBase
{
	protected EntityLiving m_toFollow;
	protected float m_minDistance;
	protected float m_minDistanceSquared;
	protected float m_maxDistance;
	protected float m_maxDistanceSquared;
	protected boolean m_avoidWaterState;
	protected int m_moveTick;
	
	public DesireFollowSpecific(RemoteEntity inEntity, EntityLiving inToFollow, float inMinDistance, float inMaxDistance)
	{
		super(inEntity);
		this.m_toFollow = inToFollow;
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSquared = this.m_minDistance * this.m_minDistance;
		this.m_maxDistance = inMaxDistance;
		this.m_maxDistanceSquared = this.m_maxDistance * this.m_maxDistance;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.m_toFollow == null)
			return false;
		else if(!this.m_toFollow.isAlive())
			return false;
		else if(this.m_toFollow.e(this.getEntityHandle()) < this.m_minDistanceSquared)
			return false;
		
		return true;
	}
	
	@Override
	public void startExecuting()
	{
		this.m_avoidWaterState = this.getEntityHandle().getNavigation().a();
		this.getEntityHandle().getNavigation().a(false);
		this.m_moveTick = 0;
	}
	
	@Override
	public void stopExecuting()
	{
		this.getEntityHandle().getNavigation().g();
		this.getEntityHandle().getNavigation().a(this.m_avoidWaterState);
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.getEntityHandle().getNavigation().f() && this.m_toFollow.e(this.getEntityHandle()) > this.m_maxDistanceSquared;
	}
	
	@Override
	public boolean update()
	{
		this.getEntityHandle().getControllerLook().a(this.m_toFollow, 10, this.getEntityHandle().bp());
		if(--this.m_moveTick <= 0)
		{
			this.m_moveTick = 10;
			if(!this.getRemoteEntity().move((LivingEntity)this.m_toFollow))
			{
				if(this.getEntityHandle().e(this.m_toFollow) >= 144)
				{
					int x = MathHelper.floor(this.m_toFollow.locX) - 2;
					int z = MathHelper.floor(this.m_toFollow.locZ) - 2;
					int y = MathHelper.floor(this.m_toFollow.boundingBox.b);
					
					for(int i = 0; i <= 4; i++)
					{
						for(int l = 0; l <= 4; l++)
						{
							if((i < 1 || l < 1 || i > 3 || l > 3) && this.getEntityHandle().world.v(x + i, y - 1, z + l) && !this.getEntityHandle().world.t(x + i, y, z + l) && !this.getEntityHandle().world.t(x + i, y + 1, z + l))
							{
								this.getEntityHandle().setPositionRotation((x + i + 0.5), y, (z + l + 0.5), this.getEntityHandle().yaw, this.getEntityHandle().pitch);
								this.getEntityHandle().getNavigation().g();
								return true;
							}
						}
					}
				}
			}
		}
		return true;
	}
}
