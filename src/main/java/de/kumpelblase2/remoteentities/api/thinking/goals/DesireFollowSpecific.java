package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.MathHelper;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will try and follow the specific entity.
 */
public class DesireFollowSpecific extends DesireBase
{
	@SerializeAs(pos = 1)
	protected EntityLiving m_toFollow;
	@SerializeAs(pos = 2)
	protected float m_minDistance;
	protected float m_minDistanceSquared;
	@SerializeAs(pos = 3)
	protected float m_maxDistance;
	protected float m_maxDistanceSquared;
	protected boolean m_avoidWaterState;
	protected int m_moveTick;

	@Deprecated
	public DesireFollowSpecific(RemoteEntity inEntity, LivingEntity inToFollow, float inMinDistance, float inMaxDistance)
	{
		this(inEntity, ((CraftLivingEntity)inToFollow).getHandle(), inMinDistance, inMaxDistance);
	}

	@Deprecated
	public DesireFollowSpecific(RemoteEntity inEntity, EntityLiving inToFollow, float inMinDistance, float inMaxDistance)
	{
		super(inEntity);
		this.m_toFollow = inToFollow;
		this.m_type = DesireType.FULL_CONCENTRATION;
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSquared = this.m_minDistance * this.m_minDistance;
		this.m_maxDistance = inMaxDistance;
		this.m_maxDistanceSquared = this.m_maxDistance * this.m_maxDistance;
	}

	public DesireFollowSpecific(LivingEntity inToFollow, float inMinDistance, float inMaxDistance)
	{
		this(((CraftLivingEntity)inToFollow).getHandle(), inMinDistance, inMaxDistance);
	}

	public DesireFollowSpecific(EntityLiving inToFollow, float inMinDistance, float inMaxDistance)
	{
		super();
		this.m_toFollow = inToFollow;
		this.m_type = DesireType.FULL_CONCENTRATION;
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
		else if(this.m_toFollow == this.getEntityHandle())
			return false;
		else if(this.m_toFollow.e(this.getEntityHandle()) < this.m_minDistanceSquared)
			return false;

		return true;
	}

	@Override
	public void startExecuting()
	{
		this.m_avoidWaterState =this.getNavigation().a();
		this.getNavigation().a(false);
		this.m_moveTick = 0;
	}

	@Override
	public void stopExecuting()
	{
		this.getNavigation().h();
		this.getNavigation().a(this.m_avoidWaterState);
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g() && this.m_toFollow.e(this.getEntityHandle()) > this.m_minDistanceSquared;
	}

	@Override
	public boolean update()
	{
		NMSUtil.getControllerLook(this.getEntityHandle()).a(this.m_toFollow, 10, NMSUtil.getMaxHeadRotation(this.getEntityHandle()));
		if(--this.m_moveTick <= 0)
		{
			this.m_moveTick = 10;
			if(!this.getRemoteEntity().move((LivingEntity)this.m_toFollow.getBukkitEntity()))
			{
				if(this.getEntityHandle().e(this.m_toFollow) >= this.m_maxDistanceSquared)
				{
					int x = MathHelper.floor(this.m_toFollow.locX) - 2;
					int z = MathHelper.floor(this.m_toFollow.locZ) - 2;
					int y = MathHelper.floor(this.m_toFollow.boundingBox.b);

					for(int i = 0; i <= 4; i++)
					{
						for(int l = 0; l <= 4; l++)
						{
							if((i < 1 || l < 1 || i > 3 || l > 3) && this.getEntityHandle().world.q(x + i, y - 1, z + l) && !this.getEntityHandle().world.t(x + i, y, z + l) && !this.getEntityHandle().world.t(x + i, y + 1, z + l))
							{
								this.getEntityHandle().setPositionRotation((x + i + 0.5), y, (z + l + 0.5), this.getEntityHandle().yaw, this.getEntityHandle().pitch);
								this.getNavigation().h();
								return true;
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}