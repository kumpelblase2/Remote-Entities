package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.Block;
import net.minecraft.server.BlockDoor;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathPoint;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireInteractDoor extends DesireBase
{
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
		this.m_entityX = (float)(this.m_x + 0.5 - this.getRemoteEntity().getHandle().locX);
		this.m_entityZ = (float)(this.m_z + 0.5 - this.getRemoteEntity().getHandle().locZ);
	}

	@Override
	public void stopExecuting()
	{	
	}
	
	@Override
	public boolean update()
	{
		float entityX = (float)(this.m_x + 0.5 - this.getRemoteEntity().getHandle().locX);
		float entityZ = (float)(this.m_z + 0.5 - this.getRemoteEntity().getHandle().locZ);
		float dist = this.m_entityX * entityX + this.m_entityZ * entityZ;
		
		if(dist < 0)
			this.m_foundDoor = true;
		
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(!this.getRemoteEntity().getHandle().positionChanged)
			return false;
		
		Navigation nav = this.getRemoteEntity().getHandle().getNavigation();
		PathEntity path = nav.d();
		if(path != null && !path.b() && nav.c())
		{
			for(int i = 0; i < Math.min(path.e() + 2, path.d()); ++i)
			{
				PathPoint point = path.a(i);
				this.m_x = point.a;
				this.m_y = point.b + 1;
				this.m_z = point.c;
				
				if(this.getRemoteEntity().getHandle().e((double)this.m_x, (double)this.m_y, (double)this.m_z) <= 2.25)
				{
					this.m_door = this.getDoor(this.m_x, this.m_y, this.m_z);
					if(this.m_door != null)
						return true;
				}
			}
			
			this.m_x = MathHelper.floor(this.getRemoteEntity().getHandle().locX);
			this.m_y = MathHelper.floor(this.getRemoteEntity().getHandle().locY + 1);
			this.m_z = MathHelper.floor(this.getRemoteEntity().getHandle().locZ);
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
		int id = this.getRemoteEntity().getHandle().world.getTypeId(x, y, z);
		
		if((!this.m_ironDoor && id == Block.WOODEN_DOOR.id) || (this.m_ironDoor && id == Block.IRON_DOOR_BLOCK.id))
			return (BlockDoor)Block.byId[id];
		else
			return null;
	}
}
