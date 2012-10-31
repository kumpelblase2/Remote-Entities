package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.Block;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireEatGrass extends DesireBase
{
	protected int m_eatTick;
	
	public DesireEatGrass(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 7;
	}

	@Override
	public void startExecuting()
	{
		this.m_eatTick = 40;
		this.getRemoteEntity().getHandle().world.broadcastEntityEffect(this.getRemoteEntity().getHandle(), (byte)10);
		this.getRemoteEntity().getHandle().getNavigation().g();
	}

	@Override
	public void stopExecuting()
	{
		this.m_eatTick = 0;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getRemoteEntity().getHandle().au().nextInt(this.getRemoteEntity().getHandle().isBaby() ? 50 : 1000) != 0)
			return false;
		else
		{
			EntityLiving entity = this.getRemoteEntity().getHandle();
			int x = MathHelper.floor(entity.locX);
			int y = MathHelper.floor(entity.locY);
			int z = MathHelper.floor(entity.locZ);
			
			return entity.world.getTypeId(x, y, z) == Block.LONG_GRASS.id && entity.world.getData(x, y, z) == 1 ? true : entity.world.getTypeId(x, y - 1, z) == Block.GRASS.id; 
		}
	}

	@Override
	public boolean canContinue()
	{
		return this.m_eatTick > 40;
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
			EntityLiving entity = this.getRemoteEntity().getHandle();
			int x = MathHelper.floor(entity.locX);
			int y = MathHelper.floor(entity.locY);
			int z = MathHelper.floor(entity.locZ);
			
			if(entity.world.getTypeId(x, y, z) == Block.LONG_GRASS.id)
			{
				entity.world.triggerEffect(2001, x, y, z, Block.LONG_GRASS.id + 4096);
				entity.world.setTypeId(x, z, z, 0);
				entity.aA();
			}
			else if(entity.world.getTypeId(x, y - 1, z) == Block.GRASS.id)
			{
				entity.world.triggerEffect(2001, x, y, z, Block.GRASS.id);
				entity.world.setTypeId(x, y - 1, z, Block.DIRT.id);
				entity.aA();
			}
		}
		return true;
	}
}
