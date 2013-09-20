package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.*;
import net.minecraft.server.v1_6_R3.*;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.features.AgeFeature;
import de.kumpelblase2.remoteentities.api.features.MateFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * Using this desire the animal entity will try to breed a child.
 */
public class DesireBreed extends DesireBase
{
	protected EntityLiving m_mate;
	protected int m_mateTicks = 0;

	@Deprecated
	public DesireBreed(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	public DesireBreed()
	{
		super();
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	@Override
	public void stopExecuting()
	{
		this.m_mate = null;
		this.m_mateTicks = 0;
	}

	@Override
	public boolean update()
	{
		NMSUtil.getControllerLook(this.getEntityHandle()).a(this.m_mate, 10, NMSUtil.getMaxHeadRotation(this.getEntityHandle()));
		this.getRemoteEntity().move((LivingEntity)this.m_mate.getBukkitEntity());
		this.m_mateTicks++;
		if(this.m_mateTicks >= 60 && this.getEntityHandle().e(this.m_mate) < 9D)
			this.createChild();

		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getRemoteEntity().getFeatures().hasFeature(MateFeature.class))
		{
			MateFeature feature = this.getRemoteEntity().getFeatures().getFeature(MateFeature.class);
			if(!feature.isAffected())
				return false;
			else
			{
				this.m_mate = this.getNextAnimal();
				return this.m_mate != null;
			}
		}
		else if(this.getEntityHandle() instanceof EntityAnimal)
		{
			EntityAnimal entity = (EntityAnimal)this.getEntityHandle();
			if(!entity.bY())
				return false;
			else
			{
				this.m_mate = this.getNextAnimal();
				return this.m_mate != null;
			}
		}

		return false;
	}

	@Override
	public boolean canContinue()
	{
		return this.m_mate.isAlive() && /*this.m_mate.bY() &&*/ this.m_mateTicks < 60;
	}

	@SuppressWarnings("rawtypes")
	protected EntityLiving getNextAnimal()
	{
		double range = 8;
		List entities = this.getEntityHandle().world.a(this.getEntityHandle().getClass(), this.getEntityHandle().boundingBox.grow(range, range, range));
		Iterator it = entities.iterator();
		double nearestRange = Double.MAX_VALUE;
		EntityLiving nearest = null;
		EntityLiving entity = this.getEntityHandle();
		while(it.hasNext())
		{
			EntityLiving mate = (EntityLiving)it.next();
			double currentRange;
			if(this.getRemoteEntity().getFeatures().hasFeature(MateFeature.class))
			{
				MateFeature feature = this.getRemoteEntity().getFeatures().getFeature(MateFeature.class);
				if(feature.isPossiblePartner((LivingEntity)mate.getBukkitEntity()) && (currentRange = entity.e(mate)) < nearestRange)
				{
					nearest = mate;
					nearestRange = currentRange;
				}
			}
			else if(entity instanceof EntityAnimal)
			{
				if(mate instanceof EntityAnimal)
				{
					if(((EntityAnimal)entity).mate((EntityAnimal)mate) && (currentRange = entity.e(mate)) < nearestRange)
					{
						nearest = mate;
						nearestRange = currentRange;
					}
				}
			}
		}
		return nearest;
	}

	protected EntityLiving createChild()
	{
		LivingEntity baby = null;
		if(this.getRemoteEntity().getFeatures().hasFeature(MateFeature.class))
		{
			MateFeature feature = this.getRemoteEntity().getFeatures().getFeature(MateFeature.class);
			baby = feature.makeBaby();
		}
		else if(this.getEntityHandle() instanceof EntityAnimal)
			baby = (LivingEntity)((EntityAnimal)this.getEntityHandle()).createChild((EntityAnimal)this.m_mate).getBukkitEntity();

		if(baby != null)
		{
			if(this.getRemoteEntity().getFeatures().hasFeature(AgeFeature.class))
				this.getRemoteEntity().getFeatures().getFeature(AgeFeature.class).setAge(6000);
			else
				((EntityAnimal)this.getEntityHandle()).setAge(6000);

			if(this.m_mate instanceof RemoteEntityHandle && ((RemoteEntityHandle)this.m_mate).getRemoteEntity().getFeatures().hasFeature(AgeFeature.class))
				((RemoteEntityHandle)this.m_mate).getRemoteEntity().getFeatures().getFeature(AgeFeature.class).setAge(6000);
			else if(this.m_mate instanceof EntityAnimal)
				((EntityAnimal)this.m_mate).setAge(6000);

			if(this.getRemoteEntity().getFeatures().hasFeature(MateFeature.class))
				this.getRemoteEntity().getFeatures().getFeature(MateFeature.class).resetAffection();
			else
				((EntityAnimal)this.getEntityHandle()).bZ();

			if(this.m_mate instanceof RemoteEntityHandle && ((RemoteEntityHandle)this.m_mate).getRemoteEntity().getFeatures().hasFeature(MateFeature.class))
				((RemoteEntityHandle)this.m_mate).getRemoteEntity().getFeatures().getFeature(MateFeature.class).resetAffection();
			else if(this.m_mate instanceof EntityAnimal)
				((EntityAnimal)this.m_mate).bZ();

			EntityLiving entity = this.getEntityHandle();
			if(baby instanceof RemoteEntityHandle && ((RemoteEntityHandle)baby).getRemoteEntity().getFeatures().hasFeature(AgeFeature.class))
				((RemoteEntityHandle)baby).getRemoteEntity().getFeatures().getFeature(AgeFeature.class).setAge(-24000);
			else if(baby instanceof EntityAgeable)
				((EntityAgeable)baby).setAge(-24000);

			((CraftLivingEntity)baby).getHandle().setPositionRotation(entity.locX, entity.locY, entity.locZ, 0, 0);
			entity.world.addEntity(((CraftLivingEntity)baby).getHandle(), SpawnReason.BREEDING);
			Random r = entity.aD();
			for(int i = 0; i < 7; ++i)
			{
				double d0 = r.nextGaussian() * 0.02D;
				double d1 = r.nextGaussian() * 0.02D;
				double d2 = r.nextGaussian() * 0.02D;

				entity.world.addParticle("heart", entity.locX + (r.nextFloat() * entity.width * 2) - entity.width, entity.locY + 0.5D + (r.nextFloat() * entity.length), entity.locZ + (r.nextFloat() * entity.width * 2) - entity.width, d0, d1, d2);
			}

			entity.world.addEntity(new EntityExperienceOrb(entity.world, entity.locX, entity.locY, entity.locZ, r.nextInt(7) + 1));
			return ((CraftLivingEntity)baby).getHandle();
		}

		return null;
	}
}
