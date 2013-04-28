package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import net.minecraft.server.v1_5_R2.*;

public class DesireRestrictOpenDoor extends DesireBase
{
	protected VillageDoor m_door;	
	
	public DesireRestrictOpenDoor(RemoteEntity inEntity)
	{
		super(inEntity);
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null || entity.world.u())
			return false;
		else
		{
			Village nearestVillage = entity.world.villages.getClosestVillage(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ), 16);
			if(nearestVillage == null)
				return false;
			else
			{
				this.m_door = nearestVillage.b(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ));
				return this.m_door != null && this.m_door.c(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ)) < 2.25;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving entity = this.getEntityHandle();
		return !entity.world.u() && !this.m_door.removed && this.m_door.a(MathHelper.floor(entity.locX), MathHelper.floor(entity.locZ));
	}
	
	@Override
	public void startExecuting()
	{
		this.getEntityHandle().getNavigation().b(false);
		this.getEntityHandle().getNavigation().c(false);
	}
	
	@Override
	public void stopExecuting()
	{
		this.getEntityHandle().getNavigation().b(true);
		this.getEntityHandle().getNavigation().c(true);
		this.m_door = null;
	}
	
	@Override
	public boolean update()
	{
		this.m_door.e();
		return true;
	}
}
