package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_5.Container;
import net.minecraft.server.v1_4_5.EntityHuman;
import net.minecraft.server.v1_4_5.EntityVillager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.NotAVillagerException;

public class DesireTradeWithPlayer extends DesireBase
{
	protected EntityVillager m_villager;
	
	public DesireTradeWithPlayer(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityVillager))
			throw new NotAVillagerException();
		
		this.m_villager = (EntityVillager)this.getEntityHandle();
		this.m_type = 5;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		if(!this.getEntityHandle().isAlive())
			return false;
		else if(this.getEntityHandle().H())
			return false;
		else if(!this.getEntityHandle().onGround)
			return false;
		else if(this.getEntityHandle().velocityChanged)
			return false;
		else
		{
			EntityHuman trader = this.m_villager.m_();
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
		this.m_villager.b_(null);
	}
}
