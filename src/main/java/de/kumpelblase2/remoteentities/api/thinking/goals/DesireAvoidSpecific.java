package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.List;
import net.minecraft.server.v1_5_R2.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSClassMap;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class DesireAvoidSpecific extends DesireBase
{
	@SerializeAs(pos = 4)
	protected final Class<? extends Entity> m_toAvoid;
	@SerializeAs(pos = 1)
	protected float m_minDistance;
	@SerializeAs(pos = 3)
	protected float m_farSpeed;
	@SerializeAs(pos = 2)
	protected float m_closeSpeed;
	protected Entity m_closestEntity;
	protected PathEntity m_path;
	
	@SuppressWarnings("unchecked")
	public DesireAvoidSpecific(RemoteEntity inEntity, float inMinDistance, float inCloseSpeed, float inFarSpeed, Class<?> inToAvoid)
	{
		super(inEntity);
		if(Entity.class.isAssignableFrom(inToAvoid))
			this.m_toAvoid = (Class<? extends Entity>)inToAvoid;
		else
			this.m_toAvoid = (Class<? extends Entity>)NMSClassMap.getNMSClass(inToAvoid);
		
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
		this.movePath(this.m_path, this.m_farSpeed);
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

        if (!this.getEntityHandle().aD().canSee(this.m_closestEntity))
            return false;
        else
        {
            Vec3D var2 = de.kumpelblase2.remoteentities.nms.RandomPositionGenerator.b(this.getEntityHandle(), 16, 7, Vec3D.a.create(this.m_closestEntity.locX, this.m_closestEntity.locY, this.m_closestEntity.locZ));

            if (var2 == null)
                return false;
            else if (this.m_closestEntity.e(var2.c, var2.d, var2.e) < this.m_closestEntity.e(this.getEntityHandle()))
                return false;
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
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
