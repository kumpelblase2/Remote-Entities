package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.DistanceComparator;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireAttackNearest extends DesireTargetBase
{
	protected int m_targetChance;
	protected Class<? extends EntityLiving> m_targetClass;
	protected DistanceComparator m_comparator;
	protected EntityLiving m_target;
	protected boolean m_onlyAtNight;
	
	public DesireAttackNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, int inChance)
	{
		this(inEntity, inTargetClass, inDistance, inShouldCheckSight, false, inChance);
	}	
	
	public DesireAttackNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele, int inChance)
	{
		super(inEntity, inDistance, inShouldCheckSight, inShouldMeele);
		this.m_comparator = new DistanceComparator(null, this.getRemoteEntity().getHandle());
		this.m_targetChance = inChance;
		this.m_targetClass = inTargetClass;
		this.m_onlyAtNight = false;
		this.m_type = 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean shouldExecute()
	{
		if(this.m_onlyAtNight && this.getRemoteEntity().getHandle().world.t())
			return false;
		else if(this.m_targetChance > 0 && this.getRemoteEntity().getHandle().aA().nextInt(this.m_targetChance) != 0)
			return false;
		else
		{
			if(this.m_targetClass == EntityHuman.class)
			{
				EntityHuman human = this.getRemoteEntity().getHandle().world.findNearbyVulnerablePlayer(this.getRemoteEntity().getHandle(), this.m_distance);
				
				if(this.isSuitableTarget(human, false))
				{
					this.m_target = human;
					return true;
				}
			}
			else
			{
				List<EntityLiving> entities = this.getRemoteEntity().getHandle().world.a(this.m_targetClass, this.getRemoteEntity().getHandle().boundingBox.grow(this.m_distance, 4, this.m_distance));
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
		this.getRemoteEntity().getHandle().b(this.m_target);
		super.startExecuting();
	}
}
