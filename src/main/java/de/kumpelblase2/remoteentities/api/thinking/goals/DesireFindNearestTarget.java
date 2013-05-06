package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.persistence.*;
import de.kumpelblase2.remoteentities.utilities.*;
import net.minecraft.server.v1_5_R3.*;
import java.util.*;

public class DesireFindNearestTarget extends DesireTargetBase
{
	@SerializeAs(pos = 5)
	protected int m_targetChance;
	@SerializeAs(pos = 4)
	protected Class<? extends Entity> m_targetClass;
	protected DistanceComparator m_comparator;
	protected EntityLiving m_target;
	@SerializeAs(pos = 6)
	protected IEntitySelector m_selector;
	protected boolean m_onlyAtNight;
	
	public DesireFindNearestTarget(RemoteEntity inEntity, Class<?> inTargetClass, float inDistance, boolean inShouldCheckSight, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, false, inChance);
	}	
	
	public DesireFindNearestTarget(RemoteEntity inEntity, Class<?> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, inShouldMelee, inChance, null);
	}
	
	@SuppressWarnings("unchecked")
	public DesireFindNearestTarget(RemoteEntity inEntity, Class<?> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee, int inChance, IEntitySelector inSelector)
	{
		super(inEntity, inDistance, inShouldCheckSight, inShouldMelee);
		this.m_comparator = new DistanceComparator(null, this.getEntityHandle());
		this.m_targetChance = inChance;
		if(Entity.class.isAssignableFrom(inTargetClass))
			this.m_targetClass = (Class<? extends Entity>)inTargetClass;
		else
			this.m_targetClass = (Class<? extends Entity>)NMSClassMap.getNMSClass(inTargetClass);
		
		this.m_onlyAtNight = false;
		this.m_type = DesireType.PRIMAL_INSTINCT;
		this.m_selector = inSelector;
	}
	
	public DesireFindNearestTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inMelee, Class<? extends EntityLiving> inTargetClass, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, inMelee, inChance);
	}
	
	public DesireFindNearestTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inMelee, Class<? extends EntityLiving> inTargetClass, int inChange, IEntitySelector inSelector)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, inMelee, inChange, inSelector);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.m_onlyAtNight && this.getEntityHandle().world.v())
			return false;
		else if(this.m_targetChance > 0 && this.getEntityHandle().aE().nextInt(this.m_targetChance) != 0)
			return false;
		else
		{
			if(this.m_targetClass == EntityHuman.class)
			{
				EntityHuman human = this.getEntityHandle().world.findNearbyVulnerablePlayer(this.getEntityHandle(), this.m_distance);
				
				if(this.isSuitableTarget(human, false))
				{
					this.m_target = human;
					return true;
				}
			}
			else
			{
				List<EntityLiving> entities = this.getEntityHandle().world.a(this.m_targetClass, this.getEntityHandle().boundingBox.grow(this.m_distance, 4, this.m_distance), this.m_selector);
				Collections.sort(entities, this.m_comparator);
				Iterator<EntityLiving> it = entities.iterator();
				while(it.hasNext())
				{
					EntityLiving entity = it.next();
					if(this.isSuitableTarget(entity, false))
					{
						this.m_target = entity;
						return true;
					}
				}
			}
			return false;
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.getEntityHandle().setGoalTarget(this.m_target);
		super.startExecuting();
	}
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
