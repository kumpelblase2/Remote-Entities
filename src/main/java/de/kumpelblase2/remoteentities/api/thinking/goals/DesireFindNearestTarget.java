package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Collections;
import java.util.List;
import net.minecraft.server.v1_6_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.*;

/**
 * Using this desire the entity will search for the nearest entity of the given type and sets it as the next target.
 */
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
		this.m_comparator = new DistanceComparator(this.getEntityHandle());
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
		else if(this.m_targetChance > 0 && this.getEntityHandle().aB().nextInt(this.m_targetChance) != 0)
			return false;
		else
		{
			List<EntityLiving> entities = this.getEntityHandle().world.a(this.m_targetClass, this.getEntityHandle().boundingBox.grow(this.m_distance, 4, this.m_distance), this.m_selector);
			Collections.sort(entities, this.m_comparator);
			for(EntityLiving entity : entities)
			{
				if(this.isSuitableTarget(entity, false))
				{
					this.m_target = entity;
					return true;
				}
			}

			return false;
		}
	}

	@Override
	public void startExecuting()
	{
		NMSUtil.setGoalTarget(this.getEntityHandle(), this.m_target);
		super.startExecuting();
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}