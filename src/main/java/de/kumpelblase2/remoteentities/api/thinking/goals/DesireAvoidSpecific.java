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
		this.getRemoteEntity().getHandle().getNavigation().a(this.m_path, this.m_farSpeed);
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
		
		if(this.getRemoteEntity().getHandle().e(this.m_closestEntity) > 49)
			this.getRemoteEntity().getHandle().getNavigation().a(this.m_farSpeed);
		else
			this.getRemoteEntity().getHandle().getNavigation().a(this.m_closeSpeed);
		
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_toAvoid == EntityHuman.class)
        {
            if(this.getRemoteEntity().getHandle() instanceof EntityTameableAnimal && ((EntityTameableAnimal)this.getRemoteEntity().getHandle()).isTamed())
                return false;

            this.m_closestEntity = this.getRemoteEntity().getHandle().world.findNearbyPlayer(this.getRemoteEntity().getHandle(), (double)this.m_minDistance);

            if(this.m_closestEntity == null)
                return false;
        }
        else
        {
            @SuppressWarnings("rawtypes")
			List var1 = this.getRemoteEntity().getHandle().world.a(this.m_toAvoid, this.getRemoteEntity().getHandle().boundingBox.grow((double)this.m_minDistance, 3.0D, (double)this.m_minDistance));

            if(var1.isEmpty())
                return false;

            this.m_closestEntity = (Entity)var1.get(0);
        }

        if (!this.getRemoteEntity().getHandle().at().canSee(this.m_closestEntity))
        {
            return false;
        }
        else
        {
            Vec3D var2 = de.kumpelblase2.remoteentities.nms.RandomPositionGenerator.b(this.getRemoteEntity().getHandle(), 16, 7, Vec3D.a().create(this.m_closestEntity.locX, this.m_closestEntity.locY, this.m_closestEntity.locZ));

            if (var2 == null)
            {
                return false;
            }
            else if (this.m_closestEntity.e(var2.a, var2.b, var2.c) < this.m_closestEntity.e(this.getRemoteEntity().getHandle()))
            {
                return false;
            }
            else
            {
                this.m_path = this.getRemoteEntity().getHandle().getNavigation().a(var2.a, var2.b, var2.c);
                return this.m_path == null ? false : this.m_path.b(var2);
            }
        }
	}

	@Override
	public boolean canContinue()
	{
		return !this.getRemoteEntity().getHandle().getNavigation().f();
	}
}
