package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityVillager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireTradeWithPlayer extends DesireBase
{
	protected EntityVillager m_villager;
	
	public DesireTradeWithPlayer(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getRemoteEntity().getHandle() instanceof EntityVillager))
			throw new Exception("Entity needs to be a villager");
		
		this.m_villager = (EntityVillager)this.getRemoteEntity().getHandle();
		this.m_type = 5;
	}

	@Override
	public boolean shouldExecute()
	{
		if(!this.getRemoteEntity().getHandle().isAlive())
			return false;
		else if(this.getRemoteEntity().getHandle().H())
			return false;
		else if(!this.getRemoteEntity().getHandle().onGround)
			return false;
		else if(this.getRemoteEntity().getHandle().velocityChanged)
			return false;
		else
		{
			EntityHuman trader = this.m_villager.l_();
			return trader == null ? false : this.m_villager.e(trader) > 16 ? false : (trader.activeContainer instanceof Container);
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.m_villager.getNavigation().g();
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_villager.a_(null);
	}
}
