package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import net.minecraft.server.v1_4_R1.*;

public class DesireLookAtNearest extends DesireBase
{
	protected EntityLiving m_target;
	protected Class<? extends EntityLiving> m_toLookAt;
	protected int m_lookTicks;
	protected float m_minDist;
	protected float m_minDistSquared;
	protected float m_lookPossibility;
	
	public DesireLookAtNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inMinDistance)
	{
		this(inEntity, inTarget, inMinDistance, 0.02F);
	}
	
	public DesireLookAtNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inMinDistance, float inPossibility)
	{
		super(inEntity);
		this.m_toLookAt = inTarget;
		this.m_minDist = inMinDistance;
		this.m_minDistSquared = this.m_minDist * this.m_minDist;
		this.m_lookPossibility = inPossibility;
	}

    public DesireLookAtNearest(ParameterData[] parameters)
    {
        this(((RemoteEntity)parameters[0].value), (Class<? extends EntityLiving>)parameters[1].value, (Float)parameters[2].value, (Float)parameters[3].value);
    }


    @Override
	public void startExecuting()
	{
		this.m_lookTicks = 40 + this.getEntityHandle().aB().nextInt(40);
	}

	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}

	@Override
	public boolean update()
	{
		this.getEntityHandle().getControllerLook().a(this.m_target.locX, this.m_target.locY + this.m_target.getHeadHeight(), this.m_target.locZ, 10, this.getEntityHandle().bp());
		this.m_lookTicks--;
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null)
			return false;
		
		if(entity.aB().nextFloat() >= this.m_lookPossibility)
			return false;
		else
		{
			if(this.m_toLookAt == EntityHuman.class || this.m_toLookAt == EntityPlayer.class)
				this.m_target = entity.world.findNearbyPlayer(entity, this.m_minDist);
			else
				this.m_target = (EntityLiving)entity.world.a(this.m_toLookAt, entity.boundingBox.grow(this.m_minDist, 3, this.m_minDist), entity);
			
			return this.m_target != null;
		}
	}

	@Override
	public boolean canContinue()
	{
		return !this.m_target.isAlive() ? false : (this.getEntityHandle().e(this.m_target) > this.m_minDistSquared ? false : this.m_lookTicks > 0);
	}

    @Override
    public Object[] getConstructionals()
    {
        Object[] constructionals = new Object[4];
        constructionals[0] = this.getRemoteEntity();

        Class<? extends EntityLiving> target = EntityLiving.class;
        Class[] lookAt = new Class[2];
        lookAt[0] = this.m_toLookAt;
        lookAt[1] = target;

        constructionals[1] = lookAt;
        constructionals[2] = this.m_minDist;
        constructionals[3] = this.m_lookPossibility;

        return constructionals;
    }
}
