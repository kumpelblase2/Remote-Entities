package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Village;
import net.minecraft.server.VillageDoor;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

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
		EntityLiving entity = this.getRemoteEntity().getHandle();
		if(entity.world.t())
			return false;
		else
		{
			Village nearestVillage = entity.world.villages.getClosestVillage(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ), 16);
			if(nearestVillage == null)
				return false;
			else
			{
				this.m_door = nearestVillage.b(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ));
				return this.m_door == null ? false : this.m_door.c(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ)) < 2.25;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		return entity.world.t() ? false : !this.m_door.removed && this.m_door.a(MathHelper.floor(entity.locX), MathHelper.floor(entity.locZ));
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().b(false);
		this.getRemoteEntity().getHandle().getNavigation().c(false);
	}
	
	@Override
	public void stopExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().b(true);
		this.getRemoteEntity().getHandle().getNavigation().c(true);
		this.m_door = null;
	}
	
	@Override
	public boolean update()
	{
		this.m_door.e();
		return true;
	}
}
