package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.EntityAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.CantBreedException;

public class DesireFollowParent extends DesireBase
{
	protected EntityAnimal m_animal;
	protected EntityAnimal m_parent;
	protected int m_moveTick;
	
	public DesireFollowParent(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getRemoteEntity().getHandle() instanceof EntityAnimal))
			throw new CantBreedException();
		
		this.m_animal = (EntityAnimal)this.getRemoteEntity().getHandle();
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean shouldExecute()
	{
		if(this.m_animal.getAge() >= 0)
			return false;
		else
		{
			List animals = this.m_animal.world.a(this.m_animal.getClass(), this.m_animal.boundingBox.grow(8, 4, 8));
			EntityAnimal nearest = null;
			double minDist = Double.MAX_VALUE;
			Iterator it = animals.iterator();
			while(it.hasNext())
			{
				EntityAnimal animal = (EntityAnimal)it.next();
				if(animal.getAge() >= 0)
				{
					double distance = this.m_animal.e(animal);
					if(distance <= minDist)
					{
						minDist = distance;
						nearest = animal;
					}
				}
			}
			
			if(nearest == null)
				return false;
			else if(minDist < 9)
				return false;
			else
			{
				this.m_parent = nearest;
				return true;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		if(!this.m_parent.isAlive())
			return false;
		else
		{
			double dist = this.m_animal.e(this.m_parent);
			return dist >= 9 && dist <= 256;
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.m_moveTick = 0;
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_parent = null;
	}
	
	@Override
	public boolean update()
	{
		if(--this.m_moveTick <= 0)
		{
			this.m_moveTick = 10;
			this.m_animal.getNavigation().a(this.m_parent, this.getRemoteEntity().getSpeed());
		}		
		return true;
	}
}
