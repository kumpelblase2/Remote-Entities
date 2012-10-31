package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireAttackTarget extends DesireTargetBase
{
	protected boolean m_attackNearest;
	protected EntityLiving m_target;
	
	
	public DesireAttackTarget(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inAttackNearest)
	{
		super(inEntity, inDistance, inShouldCheckSight);
		this.m_attackNearest = inAttackNearest;
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		return this.isSuitableTarget(this.getRemoteEntity().getHandle().av(), true);
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving entityTarget = this.getRemoteEntity().getHandle().av();
		return entityTarget != null && entityTarget != this.m_target; 
	}
	
	@Override
	public void startExecuting()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		entity.b(entity.av());
		this.m_target = entity.av();
		
		if(this.m_attackNearest)
		{
			@SuppressWarnings("unchecked")
			List<EntityLiving> list = entity.world.a(this.m_target.getClass(), AxisAlignedBB.a().a(entity.locX, entity.locY, entity.locZ, entity.locX + 1, entity.locY + 1, entity.locZ + 1).grow(this.m_distance, 4, this.m_distance));
			Iterator<EntityLiving> it = list.iterator();
			
			while(it.hasNext())
			{
				EntityLiving target = it.next();
				
				if(this.getRemoteEntity().getHandle() != target && target.az() == null)
					target.b(entity.av());
			}
		}
		super.startExecuting();
	}
}
