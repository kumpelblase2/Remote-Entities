package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.RidingFeature;
import de.kumpelblase2.remoteentities.api.features.TamedRidingFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.exceptions.NotRideableException;

public class DesireTameByRiding extends DesireBase
{
	protected double m_x;
	protected double m_y;
	protected double m_z;
	protected double m_speed;

	public DesireTameByRiding(RemoteEntity inEntity)
	{
		this(inEntity, 1.2d);
	}

	public DesireTameByRiding(RemoteEntity inEntity, double inSpeed)
	{
		super(inEntity);
		if(!(inEntity.getHandle() instanceof EntityHorse) && !inEntity.getFeatures().hasFeature(RidingFeature.class))
			throw new NotRideableException();

		this.m_speed = inSpeed;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		if(!this.canBeRidden() && this.getEntityHandle().passenger != null)
		{
			Vec3D vec = de.kumpelblase2.remoteentities.nms.RandomPositionGenerator.a(this.getEntityHandle(), 5, 4);

			if(vec == null)
				return false;
			else
			{
				this.m_x = vec.c;
				this.m_y = vec.d;
				this.m_z = vec.e;
				return true;
			}
		}

		return false;
	}

	@Override
	public void startExecuting()
	{
		this.getNavigation().a(this.m_x, this.m_y, this.m_z, this.m_speed);
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g() && this.getEntityHandle().passenger != null;
	}

	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity.aB().nextInt(50) == 0)
		{
			if(entity.passenger instanceof EntityHuman)
			{
				int i = this.getRideableChance();
				int j = 100; //horse.cm()

				if(j > 0 && entity.aB().nextInt(j) < i)
				{
					this.setRideable();
					entity.world.broadcastEntityEffect(entity, (byte)7);
					return false;
				}

				this.increseRideableChance(5);
			}

			entity.passenger.mount(null);
			entity.passenger = null;
			if(entity instanceof EntityHorse)
				((EntityHorse)entity).cz();

			entity.world.broadcastEntityEffect(entity, (byte)6);
		}

		return true;
	}

	protected void setRideable()
	{
		if(this.getEntityHandle() instanceof EntityHorse)
			((EntityHorse)this.getEntityHandle()).g((EntityHuman)((EntityHorse)this.getEntityHandle()).passenger);
		else
		{
			RidingFeature ridingFeature = this.getRemoteEntity().getFeatures().getFeature(RidingFeature.class);
			ridingFeature.setRideable(true);
			if(ridingFeature instanceof TamedRidingFeature)
				((TamedRidingFeature)ridingFeature).tame((Player)this.getEntityHandle().passenger.getBukkitEntity());
		}
	}

	protected boolean canBeRidden()
	{
		if(this.getEntityHandle() instanceof EntityHorse)
			return ((EntityHorse)this.getEntityHandle()).bS();
		else
			return this.getRemoteEntity().getFeatures().getFeature(RidingFeature.class).isPreparedToRide();
	}

	protected void increseRideableChance(int inIncrease)
	{
		if(this.getEntityHandle() instanceof EntityHorse)
			((EntityHorse)this.getEntityHandle()).t(inIncrease);
		else
			this.getRemoteEntity().getFeatures().getFeature(RidingFeature.class).increaseRideableChance(inIncrease);
	}

	protected int getRideableChance()
	{
		if(this.getEntityHandle() instanceof EntityHorse)
			return ((EntityHorse)this.getEntityHandle()).cg();
		else
			return this.getRemoteEntity().getFeatures().getFeature(RidingFeature.class).getRideableChance();
	}
}