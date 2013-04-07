package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.EntityTameableAnimal;
import net.minecraft.server.v1_5_R2.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.exceptions.NotTameableException;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class DesireFollowTamer extends DesireBase
{
	protected EntityLiving m_animal;
	@SerializeAs(pos = 1)
	protected float m_minDistance;
	protected float m_minDistanceSquared;
	@SerializeAs(pos = 2)
	protected float m_maxDistance;
	protected float m_maxDistanceSquared;
	protected EntityLiving m_owner;
	protected int m_moveTick;
	protected boolean m_avoidWaterState;
	
	public DesireFollowTamer(RemoteEntity inEntity, float inMinDistance, float inMaxDistance) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal) && !this.getRemoteEntity().getFeatures().hasFeature(TamingFeature.class))
			throw new NotTameableException();
		
		this.m_animal = this.getEntityHandle();
		this.m_type = DesireType.FULL_CONCENTRATION;
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSquared = this.m_minDistance * this.m_minDistance;
		this.m_maxDistance = inMaxDistance;
		this.m_maxDistanceSquared = this.m_maxDistance * this.m_maxDistance;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_animal == null)
			return false;
		
		if(!this.isTamed())
			return false;
		
		EntityLiving owner = this.getTamer();
		if(owner == null)
			return false;
		else if(this.isSitting())
			return false;
		else if(this.m_animal.e(owner) < this.m_minDistanceSquared)
			return false;
		else
		{
			this.m_owner = owner;
			return true;
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.m_animal.getNavigation().f() && this.m_animal.e(this.m_owner) > this.m_maxDistanceSquared && !this.isSitting();
	}
	
	@Override
	public void startExecuting()
	{
		this.m_moveTick = 0;
		this.m_avoidWaterState = this.m_animal.getNavigation().a();
		this.m_animal.getNavigation().a(false);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_owner = null;
		this.m_animal.getNavigation().g();
		this.m_animal.getNavigation().a(this.m_avoidWaterState);
	}
	
	@Override
	public boolean update()
	{
		this.m_animal.getControllerLook().a(this.m_owner, 10, this.m_animal.bs());
		if(!this.isSitting())
		{
			if(--this.m_moveTick <= 0)
			{
				this.m_moveTick = 10;
				if(!this.getRemoteEntity().move((LivingEntity)this.m_owner.getBukkitEntity()))
				{
					if(this.m_animal.e(this.m_owner) >= 144)
					{
						int x = MathHelper.floor(this.m_owner.locX) - 2;
						int z = MathHelper.floor(this.m_owner.locZ) - 2;
						int y = MathHelper.floor(this.m_owner.boundingBox.b);
						
						for(int i = 0; i <= 4; i++)
						{
							for(int l = 0; l <= 4; l++)
							{
								if((i < 1 || l < 1 || i > 3 || l > 3) && this.m_animal.world.w(x + i, y - 1, z + l) && !this.m_animal.world.u(x + i, y, z + l) && !this.m_animal.world.u(x + i, y + 1, z + l))
								{
									this.m_animal.setPositionRotation((x + i + 0.5), y, (z + l + 0.5), this.m_animal.yaw, this.m_animal.pitch);
									this.m_animal.getNavigation().g();
									return true;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	protected EntityLiving getTamer()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).getOwner();
		else
		{
			Player pl = this.getRemoteEntity().getFeatures().getFeature(TamingFeature.class).getTamer();
			if(pl == null)
				return null;
			
			return ((CraftPlayer)pl).getHandle();
		}
	}
	
	protected boolean isSitting()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).isSitting();
		
		return false;
	}
	
	protected boolean isTamed()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).isTamed();
		else
			return this.getRemoteEntity().getFeatures().getFeature(TamingFeature.class).isTamed();
	}
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
