package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

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
		if(entity == null || entity.world.v())
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
		return !entity.world.v() && !this.m_door.removed && this.m_door.a(MathHelper.floor(entity.locX), MathHelper.floor(entity.locZ));
	}

	@Override
	public void startExecuting()
	{
		NMSUtil.getNavigation(this.getEntityHandle()).b(false);
		NMSUtil.getNavigation(this.getEntityHandle()).c(false);
	}

	@Override
	public void stopExecuting()
	{
		NMSUtil.getNavigation(this.getEntityHandle()).b(true);
		NMSUtil.getNavigation(this.getEntityHandle()).c(true);
		this.m_door = null;
	}

	@Override
	public boolean update()
	{
		this.m_door.e();
		return true;
	}
}