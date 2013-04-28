package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.event.CraftEventFactory;
import net.minecraft.server.v1_5_R2.Block;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;

public class DesireEatGrass extends DesireBase
{
	protected int m_eatTick;
	
	public DesireEatGrass(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.FOOD;
	}

	@Override
	public void startExecuting()
	{
		this.m_eatTick = 40;
		this.getEntityHandle().world.broadcastEntityEffect(this.getEntityHandle(), (byte)10);
		this.getEntityHandle().getNavigation().g();
	}

	@Override
	public void stopExecuting()
	{
		this.m_eatTick = 0;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.getEntityHandle().aE().nextInt(this.getEntityHandle().isBaby() ? 50 : 1000) != 0)
			return false;
		else
		{
			EntityLiving entity = this.getEntityHandle();
			int x = MathHelper.floor(entity.locX);
			int y = MathHelper.floor(entity.locY);
			int z = MathHelper.floor(entity.locZ);
			
			return entity.world.getTypeId(x, y, z) == Block.LONG_GRASS.id && entity.world.getData(x, y, z) == 1 || entity.world.getTypeId(x, y - 1, z) == Block.GRASS.id;
		}
	}

	@Override
	public boolean canContinue()
	{
		return this.m_eatTick > 0;
	}
	
	public int tickTime()
	{
		return this.m_eatTick;
	}
	
	@Override
	public boolean update()
	{
		this.m_eatTick = Math.max(0, this.m_eatTick - 1);
		if(this.m_eatTick == 4)
		{
			EntityLiving entity = this.getEntityHandle();
			int x = MathHelper.floor(entity.locX);
			int y = MathHelper.floor(entity.locY);
			int z = MathHelper.floor(entity.locZ);
			
			if(entity.world.getTypeId(x, y, z) == Block.LONG_GRASS.id)
			{
				if(!CraftEventFactory.callEntityChangeBlockEvent(this.getRemoteEntity().getBukkitEntity(), this.getEntityHandle().world.getWorld().getBlockAt(x, y, z), Material.AIR).isCancelled())
				{
					entity.world.setAir(x, y, z, false);
					entity.aK();
				}
			}
			else if(entity.world.getTypeId(x, y - 1, z) == Block.GRASS.id)
			{
				if(!CraftEventFactory.callEntityChangeBlockEvent(this.getRemoteEntity().getBukkitEntity(), this.getEntityHandle().world.getWorld().getBlockAt(x, y - 1, z), Material.DIRT).isCancelled())
				{
					entity.world.triggerEffect(2001, x, y, z, Block.GRASS.id);
					entity.world.setTypeIdAndData(x, y - 1, z, Block.DIRT.id, 0, 2);
					entity.aK();
				}
			}
		}
		return true;
	}
}
