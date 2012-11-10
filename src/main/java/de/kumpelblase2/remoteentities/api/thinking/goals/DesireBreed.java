package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import net.minecraft.server.EntityAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireBreed extends DesireBase
{
	protected EntityAnimal m_mate;
	protected int m_mateTicks = 0;
	
	public DesireBreed(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 3;
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
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_mate, 10, this.getRemoteEntity().getHandle().bm());
		this.getRemoteEntity().move((LivingEntity)this.m_mate.getBukkitEntity());
		this.m_mateTicks++;
		if(this.m_mateTicks == 60)
		{
			this.createChild();
		}
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(!(this.getRemoteEntity().getHandle() instanceof EntityAnimal))
			return false;
		EntityAnimal entity = (EntityAnimal)this.getRemoteEntity().getHandle();
		if(!entity.r())
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
		return this.m_mate.isAlive() && this.m_mate.r() && this.m_mateTicks < 60;
	}
	
	@SuppressWarnings("rawtypes")
	protected EntityAnimal getNextAnimal()
	{
		double range = 8;
		List entities = this.getRemoteEntity().getHandle().world.a(this.getRemoteEntity().getHandle().getClass(), this.getRemoteEntity().getHandle().boundingBox.grow(range, range, range));
		Iterator it = entities.iterator();
		
		EntityAnimal nearest;
		do
		{
			if(!it.hasNext())
				return null;
			
			nearest = (EntityAnimal)it.next();
		}
		while(!((EntityAnimal)this.getRemoteEntity().getHandle()).mate(nearest));
		return nearest;
	}
	
	protected EntityAnimal createChild()
	{
		EntityAnimal baby = ((EntityAnimal)this.getRemoteEntity().getHandle()).createChild(this.m_mate);
		
		if(baby != null)
		{
			EntityAnimal entity = (EntityAnimal)this.getRemoteEntity().getHandle();
			entity.setAge(6000);
			this.m_mate.setAge(6000);
			entity.s();
			this.m_mate.s();
			baby.setAge(-24000);
			baby.setPositionRotation(entity.locX, entity.locY, entity.locZ, 0, 0);
			entity.world.addEntity(baby, SpawnReason.BREEDING);
			Random r = entity.aA();
			for(int i = 0; i < 7; ++i)
			{
				double d0 = r.nextGaussian() * 0.02D;
				double d1 = r.nextGaussian() * 0.02D;
				double d2 = r.nextGaussian() * 0.02D;
				
				entity.world.addParticle("heart", entity.locX + (r.nextFloat() * entity.width * 2) - entity.width, entity.locY + 0.5D + (r.nextFloat() * entity.length), entity.locZ + (r.nextFloat() * entity.width * 2) - entity.width, d0, d1, d2);
			}
		}
		
		return baby;
	}
}
