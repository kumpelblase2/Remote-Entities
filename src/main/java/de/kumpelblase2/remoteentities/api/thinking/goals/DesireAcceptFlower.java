package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_4_R1.EntityAgeable;
import net.minecraft.server.v1_4_R1.EntityIronGolem;
import net.minecraft.server.v1_4_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireAcceptFlower extends DesireBase
{
	protected boolean m_takeFlower = false;
	protected EntityIronGolem m_nearestGolem;
	protected int m_takeFlowerTick;
	
	public DesireAcceptFlower(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 3;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null)
			return false;
		
		if(entity instanceof EntityAgeable && ((EntityAgeable)entity).getAge() >= 0)
			return false;
		else if(!entity.world.u())
			return false;
		else
		{
			@SuppressWarnings("unchecked")
			List<EntityIronGolem> golems = entity.world.a(EntityIronGolem.class, entity.boundingBox.grow(6, 2, 6));
			if(golems.isEmpty())
				return false;
			else
			{
				Iterator<EntityIronGolem> it = golems.iterator();
				while(it.hasNext())
				{
					EntityIronGolem golem = it.next();
					if(golem.o() > 0)
					{
						this.m_nearestGolem = golem;
						break;
					}
				}
				return this.m_nearestGolem != null;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_nearestGolem.o() > 0;
	}
	
	@Override
	public void startExecuting()
	{
		this.m_takeFlowerTick = this.getEntityHandle().aB().nextInt(320);
		this.m_takeFlower = false;
		this.m_nearestGolem.getNavigation().g();
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_nearestGolem = null;
		this.getEntityHandle().getNavigation().g();
	}
	
	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		if(this.m_nearestGolem.o() == this.m_takeFlowerTick)
		{
			entity.getNavigation().a(this.m_nearestGolem, this.getRemoteEntity().getSpeed());
			this.m_takeFlower = true;
		}
		
		if(this.m_takeFlower && entity.e(this.m_nearestGolem) < 4)
		{
			this.m_nearestGolem.e(false);
			entity.getNavigation().g();
		}
		return true;
	}
}