package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * This is a base class for desires using doors.
 */
public class DesireInteractDoor extends DesireBase
{
	@SerializeAs(pos = 1)
	protected boolean m_ironDoor;
	protected int m_x;
	protected int m_y;
	protected int m_z;
	protected BlockDoor m_door;
	protected boolean m_foundDoor;
	protected float m_entityX;
	protected float m_entityZ;

	public DesireInteractDoor(RemoteEntity inEntity, boolean inIronDoor)
	{
		super(inEntity);
		this.m_ironDoor = inIronDoor;
	}

	@Override
	public void startExecuting()
	{
		this.m_foundDoor = false;
		this.m_entityX = (float)(this.m_x + 0.5 - this.getEntityHandle().locX);
		this.m_entityZ = (float)(this.m_z + 0.5 - this.getEntityHandle().locZ);
	}

	@Override
	public boolean update()
	{
		float entityX = (float)(this.m_x + 0.5 - this.getEntityHandle().locX);
		float entityZ = (float)(this.m_z + 0.5 - this.getEntityHandle().locZ);
		float dist = this.m_entityX * entityX + this.m_entityZ * entityZ;

		if(dist < 0)
			this.m_foundDoor = true;

		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		if(!this.getEntityHandle().positionChanged)
			return false;

		Navigation nav = NMSUtil.getNavigation(this.getEntityHandle());
		PathEntity path = nav.e();
		if(path != null && !path.b() && nav.c())
		{
			for(int i = 0; i < Math.min(path.e() + 2, path.d()); ++i)
			{
				PathPoint point = path.a(i);
				this.m_x = point.a;
				this.m_y = point.b + 1;
				this.m_z = point.c;

				if(this.getEntityHandle().e((double)this.m_x, (double)this.m_y, (double)this.m_z) <= 2.25)
				{
					this.m_door = this.getDoor(this.m_x, this.m_y, this.m_z);
					if(this.m_door != null)
						return true;
				}
			}

			this.m_x = MathHelper.floor(this.getEntityHandle().locX);
			this.m_y = MathHelper.floor(this.getEntityHandle().locY + 1);
			this.m_z = MathHelper.floor(this.getEntityHandle().locZ);
			this.m_door = this.getDoor(this.m_x, this.m_y, this.m_z);
			return this.m_door != null;
		}
		else
			return false;
	}

	@Override
	public boolean canContinue()
	{
		return !this.m_foundDoor;
	}

	protected BlockDoor getDoor(int x, int y, int z)
	{
		int id = this.getEntityHandle().world.getTypeId(x, y, z);

		if((!this.m_ironDoor && id == Block.WOODEN_DOOR.id) || (this.m_ironDoor && id == Block.IRON_DOOR_BLOCK.id))
			return (BlockDoor)Block.byId[id];
		else
			return null;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}