package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.NotTameableException;

public class DesireFollowTamer extends DesireBase
{
	protected EntityLiving m_animal;
	protected float m_minDistance;
	protected float m_maxDistance;
	protected EntityLiving m_owner;
	protected int m_moveTick;
	protected boolean m_avoidWaterState;
	
	public DesireFollowTamer(RemoteEntity inEntity, float inMinDistance, float inMaxDistance) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal) && !this.getRemoteEntity().getFeatures().hasFeature(TamingFeature.class))
			throw new NotTameableException();
		
		this.m_animal = this.getEntityHandle();
		this.m_type = 3;
		this.m_maxDistance = inMaxDistance;
		this.m_minDistance = inMinDistance;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_animal == null)
			return false;
		EntityLiving owner = this.getTamer();
		if(owner == null)
			return false;
		else if(this.isSitting())
			return false;
		else if(this.m_animal.e(owner) < this.m_minDistance * this.m_minDistance)
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
		return !this.m_animal.getNavigation().f() && this.m_animal.e(this.m_owner) > this.m_maxDistance * this.m_maxDistance && !this.isSitting();
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
		this.m_animal.getControllerLook().a(this.m_owner, 10, this.m_animal.bp());
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
								if((i < 1 || l < 1 || i > 3 || l > 3) && this.m_animal.world.v(x + i, y - 1, z + l) && !this.m_animal.world.t(x + i, y, z + l) && !this.m_animal.world.t(x + i, y + 1, z + l))
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
			return ((CraftPlayer)this.getRemoteEntity().getFeatures().getFeature(TamingFeature.class).getTamer()).getHandle();
	}
	
	protected boolean isSitting()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).isSitting();
		return false;
	}
}
