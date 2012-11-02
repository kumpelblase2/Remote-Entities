package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Location;
import net.minecraft.server.Block;
import net.minecraft.server.BlockBed;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.World;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.NotAnOcelotException;

public class DesireSitOnBlock extends DesireBase
{
	protected EntityOcelot m_ocelot;
	protected int m_x = 0;
	protected int m_y = 0;
	protected int m_z = 0;
	protected int m_currentSitTick = 0;
	protected int m_d = 0;
	protected int m_maxSitTicks = 0;
	
	public DesireSitOnBlock(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getRemoteEntity().getHandle() instanceof EntityOcelot))
			throw new NotAnOcelotException();
		
		this.m_ocelot = (EntityOcelot)this.getRemoteEntity().getHandle();
		this.m_type = 5;
	}

	@Override
	public boolean shouldExecute()
	{
		return this.m_ocelot.isTamed() && !this.m_ocelot.isSitting() && this.m_ocelot.au().nextDouble() <= 0.006500000134110451D && this.isSitableBlockInRange();
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_currentSitTick <= this.m_maxSitTicks && this.m_d <= 60 && this.isSitableBlock(this.m_ocelot.world, this.m_x, this.m_y, this.m_z);
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_x + 0.5D, this.m_y + 1, this.m_z + 0.5D));
		this.m_currentSitTick = 0;
		this.m_d = 0;
		this.m_maxSitTicks = this.m_ocelot.au().nextInt(this.m_ocelot.au().nextInt(1200) + 1200) + 1200;
		if(this.getRemoteEntity().getMind().getMovementDesire(DesireSit.class) != null)
			this.getRemoteEntity().getMind().getMovementDesire(DesireSit.class).canSit(false);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_ocelot.setSitting(false);
	}
	
	@Override
	public boolean update()
	{
		this.m_currentSitTick++;
		this.m_ocelot.setSitting(true);
		if(this.m_ocelot.e(this.m_x, this.m_y + 1, this.m_z) > 1)
		{
			this.m_ocelot.setSitting(false);
			this.m_ocelot.getNavigation().a(this.m_x + 0.5D, this.m_y + 1, this.m_z + 0.5D);
			this.m_d++;
		}
		else if(!this.m_ocelot.isSitting())
			this.m_ocelot.setSitting(true);
		else
			this.m_d--;
		
		return true;
	}
	
	protected boolean isSitableBlockInRange()
	{
		int y = (int)this.m_ocelot.locY;
		double minDist = 2.147483647E9D;
		
		for(int x = (int)this.m_ocelot.locX - 8; x < (int)this.m_ocelot.locX + 8; x++)
		{
			for(int z = (int)this.m_ocelot.locZ - 8; z < (int)this.m_ocelot.locZ + 8; z++)
			{
				if(this.isSitableBlock(this.m_ocelot.world, x, y, z) && this.m_ocelot.world.isEmpty(x, y + 1, z))
				{
					double dist = this.m_ocelot.e(x, y, z);
					if(dist < minDist)
					{
						this.m_x = x;
						this.m_y = y;
						this.m_z = z;
						minDist = dist;
					}
				}
			}
		}
		
		return minDist < 2.147483647E9D;
	}
	
	protected boolean isSitableBlock(World world, int x, int y, int z)
	{
		int type = world.getTypeId(x, y, z);
		int data = world.getData(x, y, z);
		
		if(type == Block.CHEST.id)
		{
			TileEntityChest chest = (TileEntityChest)world.getTileEntity(x, y, z);
			if(chest.h < 1)
				return true;
		}
		else
		{
			if(type == Block.BURNING_FURNACE.id)
				return true;
			
			if(type == Block.BED.id && !BlockBed.a_(data))
				return true;
		}
		
		return false;
	}
}
