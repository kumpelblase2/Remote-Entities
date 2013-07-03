package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.List;
import net.minecraft.server.v1_6_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.api.thinking.selectors.EntitySelectorViewable;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.*;

/**
 * With this desire the entity will avoid the given type of entity and will run away from it once it comes near.
 */
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
	@SerializeAs(pos = 5)
	protected boolean m_ignoreOutOfSight;
	protected Entity m_closestEntity;
	protected PathEntity m_path;
	protected EntitySelectorViewable m_selector;

	public DesireAvoidSpecific(RemoteEntity inEntity, float inMinDistance, float inCloseSpeed, float inFarSpeed, Class<?> inToAvoid)
	{
		this(inEntity, inMinDistance, inCloseSpeed, inFarSpeed, inToAvoid, true);
	}

	@SuppressWarnings("unchecked")
	public DesireAvoidSpecific(RemoteEntity inEntity, float inMinDistance, float inCloseSpeed, float inFarSpeed, Class<?> inToAvoid, boolean inIgnoreOutOfSight)
	{
		super(inEntity);
		if(Entity.class.isAssignableFrom(inToAvoid))
			this.m_toAvoid = (Class<? extends Entity>)inToAvoid;
		else
			this.m_toAvoid = (Class<? extends Entity>)NMSClassMap.getNMSClass(inToAvoid);

		this.m_minDistance = inMinDistance;
		this.m_farSpeed = inFarSpeed;
		this.m_closeSpeed = inCloseSpeed;
		this.m_selector = new EntitySelectorViewable(this.getEntityHandle());
		this.m_ignoreOutOfSight = inIgnoreOutOfSight;
		this.m_type = DesireType.PRIMAL_INSTINCT;
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
			NMSUtil.getNavigation(this.getEntityHandle()).a(this.m_farSpeed);
		else
			NMSUtil.getNavigation(this.getEntityHandle()).a(this.m_closeSpeed);

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
			List var1 = this.getEntityHandle().world.a(this.m_toAvoid, this.getEntityHandle().boundingBox.grow((double)this.m_minDistance, 3.0D, (double)this.m_minDistance), this.m_selector);

            if(var1.isEmpty())
                return false;

            this.m_closestEntity = (Entity)var1.get(0);
        }

		if(!this.m_ignoreOutOfSight && !NMSUtil.getEntitySenses(this.getEntityHandle()).canSee(this.m_closestEntity))
			return false;

        Vec3D var2 = de.kumpelblase2.remoteentities.nms.RandomPositionGenerator.b(this.getEntityHandle(), 16, 7, Vec3D.a.create(this.m_closestEntity.locX, this.m_closestEntity.locY, this.m_closestEntity.locZ));

        if (var2 == null)
            return false;
        else if (this.m_closestEntity.e(var2.c, var2.d, var2.e) < this.m_closestEntity.e(this.getEntityHandle()))
        {
            Vec3D.a.release(var2);
            return false;
        }
        else
        {
            this.m_path = NMSUtil.getNavigation(this.getEntityHandle()).a(var2.c, var2.d, var2.e);
            boolean returnValue = this.m_path != null && this.m_path.b(var2);
            Vec3D.a.release(var2);
            return returnValue;
        }
	}

	@Override
	public boolean canContinue()
	{
		return !NMSUtil.getNavigation(this.getEntityHandle()).g();
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}