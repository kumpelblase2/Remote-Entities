package de.kumpelblase2.removeentities.api.thinking.goals;

import de.kumpelblase2.removeentities.api.RemoteEntity;
import de.kumpelblase2.removeentities.api.RemoteEntityType;
import de.kumpelblase2.removeentities.api.thinking.Desire;
import net.minecraft.server.*;

public class DesireLookAtNearest extends PathfinderGoalLookAtPlayer implements Desire
{
	private final RemoteEntity m_entity;
	
	public DesireLookAtNearest(RemoteEntity inEntity, RemoteEntityType inTarget, float inDelay)
	{
		this(inEntity, inTarget.getEntityClass(), inDelay);
	}
	
	public DesireLookAtNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inDelay)
	{
		super(inEntity.getHandle(), inTarget, inDelay);
		this.m_entity = inEntity;
	}
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
	
	@Override
	public void d()
	{
		super.d();
		if(this.m_entity.getHandle() instanceof EntityPlayer)
		{
			this.m_entity.getHandle().yaw = this.m_entity.getHandle().as;
		}
	}

	@Override
	public int getType()
	{
		return this.h();
	}

	@Override
	public boolean isContinous()
	{
		return this.g();
	}

	@Override
	public void startExecuting()
	{
		this.e();
	}

	@Override
	public void stopExecuting()
	{
		this.c();
	}

	@Override
	public boolean update()
	{
		this.d();
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		return this.a();
	}

	@Override
	public boolean canContinue()
	{
		return this.a();
	}
}
