package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R3.*;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.exceptions.NotAnOcelotException;

/**
 * Using this desire the entity will sometimes find a warm block to sit and rest on.
 */
public class DesireSitOnBlock extends DesireBase
{
	protected EntityOcelot m_ocelot;
	protected int m_x = 0;
	protected int m_y = 0;
	protected int m_z = 0;
	protected int m_currentSitTick = 0;
	protected int m_actionTicks = 0;
	protected int m_maxSitTicks = 0;

	@Deprecated
	public DesireSitOnBlock(RemoteEntity inEntity)
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityOcelot))
			throw new NotAnOcelotException();

		this.m_ocelot = (EntityOcelot)this.getEntityHandle();
		this.m_type = DesireType.OCCASIONAL_URGE;
	}

	public DesireSitOnBlock()
	{
		super();
		this.m_type = DesireType.OCCASIONAL_URGE;
	}

	@Override
	public void onAdd(RemoteEntity inEntity)
	{
		super.onAdd(inEntity);
		if(!(this.getEntityHandle() instanceof EntityOcelot))
			throw new NotAnOcelotException();

		this.m_ocelot = (EntityOcelot)this.getEntityHandle();
	}

	@Override
	public boolean shouldExecute()
	{
		return this.m_ocelot != null && this.m_ocelot.isTamed() && !this.m_ocelot.isSitting() && this.m_ocelot.aH().nextDouble() <= 0.006500000134110451D && this.isSitableBlockInRange();
	}

	@Override
	public boolean canContinue()
	{
		return this.m_currentSitTick <= this.m_maxSitTicks && this.m_actionTicks <= 60 && this.isSitableBlock(this.m_ocelot.world, this.m_x, this.m_y, this.m_z);
	}

	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_x + 0.5D, this.m_y + 1, this.m_z + 0.5D));
		this.m_currentSitTick = 0;
		this.m_actionTicks = 0;
		this.m_maxSitTicks = this.m_ocelot.aH().nextInt(this.m_ocelot.aH().nextInt(1200) + 1200) + 1200;
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
		if(this.getRemoteEntity().getMind().getMovementDesire(DesireSit.class) != null)
			this.getRemoteEntity().getMind().getMovementDesire(DesireSit.class).canSit(false);

		if(this.m_ocelot.e((double)this.m_x, (double)this.m_y + 1, (double)this.m_z) > 1)
		{
			this.m_ocelot.setSitting(false);
			this.getRemoteEntity().move(new Location(this.m_ocelot.world.getWorld(), this.m_x + 0.5D, this.m_y + 1, this.m_z + 0.5D));
			this.m_actionTicks++;
		}
		else if(!this.m_ocelot.isSitting())
			this.m_ocelot.setSitting(true);
		else
			this.m_actionTicks--;

		return true;
	}

	protected boolean isSitableBlockInRange()
	{
		int y = (int)this.m_ocelot.locY;
		double minDist = 2.147483647E9D;

		for(int x = (int)this.m_ocelot.locX - 8; x < (int)this.m_ocelot.locX + 8; ++x)
		{
			for(int z = (int)this.m_ocelot.locZ - 8; z < (int)this.m_ocelot.locZ + 8; ++z)
			{
				if(this.isSitableBlock(this.m_ocelot.world, x, y, z) && this.m_ocelot.world.isEmpty(x, y + 1, z))
				{
					double dist = this.m_ocelot.e((double)x, (double)y, (double)z);
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
		Block type = world.getType(x, y, z);
		int data = world.getData(x, y, z);

		if(type == Blocks.CHEST)
		{
			TileEntityChest chest = (TileEntityChest)world.getTileEntity(x, y, z);
			if(chest.o < 1)
				return true;
		}
		else
		{
			if(type == Blocks.BURNING_FURNACE)
				return true;

			if(type == Blocks.BED && !BlockBed.b(data))
				return true;
		}

		return false;
	}
}