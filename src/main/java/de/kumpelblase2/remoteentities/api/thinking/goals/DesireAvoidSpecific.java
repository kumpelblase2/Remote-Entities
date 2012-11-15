package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.List;
import net.minecraft.server.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireAvoidSpecific extends DesireBase
{
	protected final Class<? extends Entity> m_toAvoid;
	protected float m_minDistance;
	protected float m_farSpeed;
	protected float m_closeSpeed;
	protected Entity m_closestEntity;
	protected PathEntity m_path;
	
	public DesireAvoidSpecific(RemoteEntity inEntity, float inMinDistance, float inCloseSpeed, float inFarSpeed, Class<? extends Entity> inToAvoid)
	{
		super(inEntity);
		this.m_toAvoid = inToAvoid;
		this.m_minDistance = inMinDistance;
		this.m_farSpeed = inFarSpeed;
		this.m_closeSpeed = inCloseSpeed;
		this.m_type = 1;
	}

	@Override
	public int getType()
	{
		return 1;
	}

	@Override
	public void startExecuting()
	{
		this.getEntityHandle().getNavigation().a(this.m_path, this.m_farSpeed);
	}

	@Override
	public void stopExecuting()
	{
		this.m_closestEntity = null;
	}

	@Override
	public boolean update()
	{
		if(!this.m_closestEntity.isAlive())
			return false;
		
		if(this.getEntityHandle().e(this.m_closestEntity) > 49)
			this.getEntityHandle().getNavigation().a(this.m_farSpeed);
		else
			this.getEntityHandle().getNavigation().a(this.m_closeSpeed);
		
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.m_toAvoid == EntityHuman.class)
        {
            if(this.getEntityHandle() instanceof EntityTameableAnimal && ((EntityTameableAnimal)this.getEntityHandle()).isTamed())
                return false;

            this.m_closestEntity = this.getEntityHandle().world.findNearbyPlayer(this.getEntityHandle(), (double)this.m_minDistance);

            if(this.m_closestEntity == null)
                return false;
        }
        else
        {
            @SuppressWarnings("rawtypes")
			List var1 = this.getEntityHandle().world.a(this.m_toAvoid, this.getEntityHandle().boundingBox.grow((double)this.m_minDistance, 3.0D, (double)this.m_minDistance));

            if(var1.isEmpty())
                return false;

            this.m_closestEntity = (Entity)var1.get(0);
        }

        if (!this.getEntityHandle().aA().canSee(this.m_closestEntity))
        {
            return false;
        }
        else
        {
            Vec3D var2 = de.kumpelblase2.remoteentities.nms.RandomPositionGenerator.b(this.getEntityHandle(), 16, 7, Vec3D.a.create(this.m_closestEntity.locX, this.m_closestEntity.locY, this.m_closestEntity.locZ));

            if (var2 == null)
            {
                return false;
            }
            else if (this.m_closestEntity.e(var2.c, var2.d, var2.e) < this.m_closestEntity.e(this.getEntityHandle()))
            {
                return false;
            }
            else
            {
                this.m_path = this.getEntityHandle().getNavigation().a(var2.c, var2.d, var2.e);
                return this.m_path == null ? false : this.m_path.b(var2);
            }
        }
	}

	@Override
	public boolean canContinue()
	{
		return !this.getEntityHandle().getNavigation().f();
	}
}
