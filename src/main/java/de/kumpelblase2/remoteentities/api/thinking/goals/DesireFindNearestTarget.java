package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_4_R1.DistanceComparator;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class DesireFindNearestTarget extends DesireTargetBase
{
	@SerializeAs(pos = 5)
	protected int m_targetChance;
	@SerializeAs(pos = 4)
	protected Class<? extends EntityLiving> m_targetClass;
	protected DistanceComparator m_comparator;
	protected EntityLiving m_target;
	protected boolean m_onlyAtNight;
	
	public DesireFindNearestTarget(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, false, inChance);
	}	
	
	public DesireFindNearestTarget(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele, int inChance)
	{
		super(inEntity, inDistance, inShouldCheckSight, inShouldMeele);
		this.m_comparator = new DistanceComparator(null, this.getEntityHandle());
		this.m_targetChance = inChance;
		this.m_targetClass = inTargetClass;
		this.m_onlyAtNight = false;
		this.m_type = 1;
	}
	
	public DesireFindNearestTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inMelee, Class<? extends EntityLiving> inTargetClass, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, inMelee, inChance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.m_onlyAtNight && this.getEntityHandle().world.u())
			return false;
		else if(this.m_targetChance > 0 && this.getEntityHandle().aB().nextInt(this.m_targetChance) != 0)
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
				List<EntityLiving> entities = this.getEntityHandle().world.a(this.m_targetClass, this.getEntityHandle().boundingBox.grow(this.m_distance, 4, this.m_distance));
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
