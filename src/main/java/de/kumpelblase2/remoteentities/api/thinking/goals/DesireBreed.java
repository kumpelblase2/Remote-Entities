package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.*;
import net.minecraft.server.v1_6_R2.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * Using this desire the animal entity will try to breed a child.
 */
public class DesireBreed extends DesireBase
{
	protected EntityAnimal m_mate;
	protected int m_mateTicks = 0;

	public DesireBreed(RemoteEntity inEntity)
	{
		super(inEntity);
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
		if(!(this.getEntityHandle() instanceof EntityAnimal))
			return false;

		EntityAnimal entity = (EntityAnimal)this.getEntityHandle();
		if(!entity.bY())
			return false;
		else
		{
			this.m_mate = this.getNextAnimal();
			return this.m_mate != null;
		}
	}

	@Override
	public boolean canContinue()
	{
		return this.m_mate.isAlive() && this.m_mate.bY() && this.m_mateTicks < 60;
	}

	@SuppressWarnings("rawtypes")
	protected EntityAnimal getNextAnimal()
	{
		double range = 8;
		List entities = this.getEntityHandle().world.a(this.getEntityHandle().getClass(), this.getEntityHandle().boundingBox.grow(range, range, range));
		Iterator it = entities.iterator();
		double nearestRange = Double.MAX_VALUE;
		EntityAnimal nearest = null;
		EntityAnimal entity = (EntityAnimal)this.getEntityHandle();
		while(it.hasNext())
		{
			EntityAnimal mate = (EntityAnimal)it.next();
			double currentRange;
			if(entity.mate(mate) && (currentRange = entity.e(mate)) < nearestRange)
			{
				nearest = mate;
				nearestRange = currentRange;
			}
		}
		return nearest;
	}

	protected EntityAgeable createChild()
	{
		EntityAgeable baby = ((EntityAnimal)this.getEntityHandle()).createChild(this.m_mate); //TODO create feature/behavior

		if(baby != null)
		{
			EntityAnimal entity = (EntityAnimal)this.getEntityHandle();
			entity.setAge(6000);
			this.m_mate.setAge(6000);
			entity.bZ();
			this.m_mate.bZ();
			baby.setAge(-24000);
			baby.setPositionRotation(entity.locX, entity.locY, entity.locZ, 0, 0);
			entity.world.addEntity(baby, SpawnReason.BREEDING);
			Random r = entity.aC();
			for(int i = 0; i < 7; ++i)
			{
				double d0 = r.nextGaussian() * 0.02D;
				double d1 = r.nextGaussian() * 0.02D;
				double d2 = r.nextGaussian() * 0.02D;

				entity.world.addParticle("heart", entity.locX + (r.nextFloat() * entity.width * 2) - entity.width, entity.locY + 0.5D + (r.nextFloat() * entity.length), entity.locZ + (r.nextFloat() * entity.width * 2) - entity.width, d0, d1, d2);
			}

			entity.world.addEntity(new EntityExperienceOrb(entity.world, entity.locX, entity.locY, entity.locZ, r.nextInt(7) + 1));
		}

		return baby;
	}
}
