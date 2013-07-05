package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;

/**
 * Using this desire the entity will move into the nearest village and into a house to take shelter at night.
 */
public class DesireMoveIndoors extends DesireBase
{
	protected VillageDoor m_targetDoor;
	protected int m_x = -1;
	protected int m_z = -1;

	public DesireMoveIndoors(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null)
			return false;

		if((!entity.world.v() || entity.world.Q()) && !entity.world.worldProvider.g)
		{
			if(entity.aB().nextInt(50) != 0)
				return false;
			else if(this.m_x != -1 && entity.e(this.m_x, entity.locY, this.m_z) < 4)
				return false;
			else
			{
				Village nearestVillage = entity.world.villages.getClosestVillage(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ), 14);

				if(nearestVillage == null)
					return false;
				else
				{
					this.m_targetDoor = nearestVillage.c(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ));
					return this.m_targetDoor != null;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g();
	}

	@Override
	public void startExecuting()
	{
		this.m_x = -1;
		EntityLiving entity = this.getEntityHandle();
		if(entity.e(this.m_targetDoor.getIndoorsX(), entity.locY, this.m_targetDoor.getIndoorsZ()) > 256)
		{
			Vec3D vec = RandomPositionGenerator.a(entity, 14, 3, entity.world.getVec3DPool().create(this.m_targetDoor.getIndoorsX() + 0.5, this.m_targetDoor.getIndoorsY(), this.m_targetDoor.getIndoorsZ() + 0.5));
			if(vec != null)
			{
				this.getRemoteEntity().move(new Location(entity.getBukkitEntity().getWorld(), vec.c, vec.d, vec.e));
				Vec3D.a.release(vec);
			}
		}
		else
			this.getRemoteEntity().move(new Location(entity.getBukkitEntity().getWorld(), this.m_targetDoor.getIndoorsX() + 0.5, this.m_targetDoor.getIndoorsY(), this.m_targetDoor.getIndoorsZ() + 0.5));
	}

	@Override
	public void stopExecuting()
	{
		this.m_x = this.m_targetDoor.getIndoorsX();
		this.m_z = this.m_targetDoor.getIndoorsZ();
		this.m_targetDoor = null;
	}
}