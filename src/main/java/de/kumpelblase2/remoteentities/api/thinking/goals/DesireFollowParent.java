package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.exceptions.*;
import net.minecraft.server.v1_5_R2.*;
import org.bukkit.entity.*;
import java.util.*;

public class DesireFollowParent extends DesireBase
{
	protected EntityAnimal m_animal;
	protected EntityAnimal m_parent;
	protected int m_moveTick;
	
	public DesireFollowParent(RemoteEntity inEntity)
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityAnimal))
			throw new CantBreedException();
		
		this.m_animal = (EntityAnimal)this.getEntityHandle();
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
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
			this.getRemoteEntity().move((LivingEntity)this.m_parent.getBukkitEntity());
		}		
		return true;
	}
}
