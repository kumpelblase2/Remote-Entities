package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.entity.LivingEntity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireFollowSpecific extends DesireBase
{
	protected EntityLiving m_toFollow;
	protected float m_minDistance;
	protected float m_maxDistance;
	protected boolean m_avoidWaterState;
	protected int m_moveTick;
	
	public DesireFollowSpecific(RemoteEntity inEntity, EntityLiving inToFollow, float inMinDistance, float inMaxDistance)
	{
		super(inEntity);
		this.m_toFollow = inToFollow;
		this.m_minDistance = inMinDistance;
		this.m_maxDistance = inMaxDistance;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_toFollow == null)
			return false;
		else if(!this.m_toFollow.isAlive())
			return false;
		else if(this.m_toFollow.e(this.getRemoteEntity().getHandle()) < this.m_minDistance * this.m_minDistance)
			return false;
		return true;
	}
	
	@Override
	public void startExecuting()
	{
		this.m_avoidWaterState = this.getRemoteEntity().getHandle().getNavigation().a();
		this.getRemoteEntity().getHandle().getNavigation().a(false);
		this.m_moveTick = 0;
	}
	
	@Override
	public void stopExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().g();
		this.getRemoteEntity().getHandle().getNavigation().a(this.m_avoidWaterState);
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.getRemoteEntity().getHandle().getNavigation().f() && this.m_toFollow.e(this.getRemoteEntity().getHandle()) > this.m_maxDistance * this.m_maxDistance;
	}
	
	@Override
	public boolean update()
	{
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_toFollow, 10, this.getRemoteEntity().getHandle().bm());
		if(--this.m_moveTick <= 0)
		{
			this.m_moveTick = 10;
			if(!this.getRemoteEntity().move((LivingEntity)this.m_toFollow))
			{
				if(this.getRemoteEntity().getHandle().e(this.m_toFollow) >= 144)
				{
					int x = MathHelper.floor(this.m_toFollow.locX) - 2;
					int z = MathHelper.floor(this.m_toFollow.locZ) - 2;
					int y = MathHelper.floor(this.m_toFollow.boundingBox.b);
					
					for(int i = 0; i <= 4; i++)
					{
						for(int l = 0; l <= 4; l++)
						{
							if((i < 1 || l < 1 || i > 3 || l > 3) && this.getRemoteEntity().getHandle().world.t(x + i, y - 1, z + l) && ! this.getRemoteEntity().getHandle().world.s(x + i, y, z + l) && ! this.getRemoteEntity().getHandle().world.s(x + i, y + 1, z + l))
							{
								this.getRemoteEntity().getHandle().setPositionRotation((x + i + 0.5), y, (z + l + 0.5), this.getRemoteEntity().getHandle().yaw, this.getRemoteEntity().getHandle().pitch);
								this.getRemoteEntity().getHandle().getNavigation().g();
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
