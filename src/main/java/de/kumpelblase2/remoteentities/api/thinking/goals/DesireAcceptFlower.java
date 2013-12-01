package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_7_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * This desire is aimed for villages to accept flowers given by iron golems.
 */
public class DesireAcceptFlower extends DesireBase
{
	protected boolean m_takeFlower = false;
	protected EntityIronGolem m_nearestGolem;
	protected int m_takeFlowerTick;

	@Deprecated
	public DesireAcceptFlower(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	public DesireAcceptFlower()
	{
		super();
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null)
			return false;

		if(entity instanceof EntityAgeable && ((EntityAgeable)entity).getAge() >= 0)
			return false;
		else if(!entity.world.v())
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
					if(golem.bZ() > 0)
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
		return this.m_nearestGolem.q() > 0;
	}

	@Override
	public void startExecuting()
	{
		this.m_takeFlowerTick = this.getEntityHandle().aI().nextInt(320);
		this.m_takeFlower = false;
		this.m_nearestGolem.getNavigation().g();
	}

	@Override
	public void stopExecuting()
	{
		this.m_nearestGolem = null;
		this.getNavigation().h();
	}

	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		NMSUtil.getControllerLook(entity).a(this.m_nearestGolem, 30, 30);
		if(this.m_nearestGolem.bZ() == this.m_takeFlowerTick)
		{
			this.getNavigation().a(this.m_nearestGolem, this.getRemoteEntity().getSpeed());
			this.m_takeFlower = true;
		}

		if(this.m_takeFlower && entity.e(this.m_nearestGolem) < 4)
		{
			this.m_nearestGolem.a(false);
			this.getNavigation().h();
		}

		return true;
	}
}