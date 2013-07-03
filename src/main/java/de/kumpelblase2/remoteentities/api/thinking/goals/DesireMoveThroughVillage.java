package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.*;
import net.minecraft.server.v1_6_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will move through the nearest village at day.
 */
public class DesireMoveThroughVillage extends DesireBase
{
	@SerializeAs(pos = 1)
	protected boolean m_onlyNight;
	protected PathEntity m_path;
	protected VillageDoor m_nextDoor;
	protected final List<VillageDoor> m_doors = new ArrayList<VillageDoor>();

	public DesireMoveThroughVillage(RemoteEntity inEntity, boolean inOnlyNight)
	{
		super(inEntity);
		this.m_onlyNight = inOnlyNight;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		this.cleanupDoors();
		EntityLiving entity = this.getEntityHandle();
		if(entity == null || (this.m_onlyNight && entity.world.v()))
			return false;
		else
		{
			Village nearestVillage = entity.world.villages.getClosestVillage(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ), 0);

			if(nearestVillage == null)
				return false;
			else
			{
				this.m_nextDoor = this.getNearestDoor(nearestVillage);
				if(this.m_nextDoor == null)
					return false;
				else
				{
					boolean flag = NMSUtil.getNavigation(entity).c();
					NMSUtil.getNavigation(entity).b(false);
					this.m_path = NMSUtil.getNavigation(entity).a(this.m_nextDoor.locX, this.m_nextDoor.locY, this.m_nextDoor.locZ);
					NMSUtil.getNavigation(entity).b(flag);
					if(this.m_path != null)
						return true;
					else
					{
						Vec3D vec = RandomPositionGenerator.a(entity, 10, 7, entity.world.getVec3DPool().create(this.m_nextDoor.locX, this.m_nextDoor.locY, this.m_nextDoor.locZ));

						if(vec == null)
							return false;
						else
						{
							NMSUtil.getNavigation(entity).b(false);
							this.m_path = NMSUtil.getNavigation(entity).a(vec.c, vec.d, vec.e);
							NMSUtil.getNavigation(entity).b(flag);
							Vec3D.a.release(vec);
							return this.m_path != null;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean canContinue()
	{
		if(NMSUtil.getNavigation(this.getEntityHandle()).g())
			return false;
		else
		{
			float f = this.getEntityHandle().width + 4;
			return this.getEntityHandle().e((double)this.m_nextDoor.locX, (double)this.m_nextDoor.locY, (double)this.m_nextDoor.locZ) > f * f;
		}
	}

	@Override
	public void startExecuting()
	{
		this.movePath(this.m_path, this.getRemoteEntity().getSpeed());
	}

	@Override
	public void stopExecuting()
	{
		if(NMSUtil.getNavigation(this.getEntityHandle()).g() || this.getEntityHandle().e((double)this.m_nextDoor.locX, (double)this.m_nextDoor.locY, (double)this.m_nextDoor.locZ) < 16)
			this.m_doors.add(this.m_nextDoor);
	}

	protected void cleanupDoors()
	{
		if(this.m_doors.size() > 15)
			this.m_doors.remove(0);
	}

	protected boolean isDoorInList(VillageDoor inDoor)
	{
		Iterator<VillageDoor> it = this.m_doors.iterator();
		VillageDoor door;

		do
		{
			if(!it.hasNext())
				return false;

			door = it.next();
		}
		while(door.locX != inDoor.locX || door.locY != inDoor.locY || door.locZ != inDoor.locZ);

		return true;
	}

	protected VillageDoor getNearestDoor(Village inVillage)
	{
		VillageDoor nearest = null;
		int dist = Integer.MAX_VALUE;
		@SuppressWarnings("unchecked")
		List<VillageDoor> allDoors = inVillage.getDoors();
		Iterator<VillageDoor> it = allDoors.iterator();
		EntityLiving entity = this.getEntityHandle();

		while(it.hasNext())
		{
			VillageDoor door = it.next();
			int ddist = door.b(MathHelper.floor(entity.locX), MathHelper.floor(entity.locY), MathHelper.floor(entity.locZ));

			if(ddist < dist && !this.isDoorInList(door))
			{
				nearest = door;
				dist = ddist;
			}
		}

		return nearest;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}