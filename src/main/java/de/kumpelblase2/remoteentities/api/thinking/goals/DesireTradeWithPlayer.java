package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.exceptions.*;
import net.minecraft.server.v1_5_R2.*;

public class DesireTradeWithPlayer extends DesireBase
{
	protected EntityVillager m_villager;
	
	public DesireTradeWithPlayer(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityVillager))
			throw new NotAVillagerException();
		
		this.m_villager = (EntityVillager)this.getEntityHandle();
		this.m_type = DesireType.OCCASIONAL_URGE;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(!this.getEntityHandle().isAlive())
			return false;
		else if(this.getEntityHandle().G())
			return false;
		else if(!this.getEntityHandle().onGround)
			return false;
		else if(this.getEntityHandle().velocityChanged)
			return false;
		else
		{
			EntityHuman trader = this.m_villager.m_();
			if(trader == null)
				return false;

			return this.m_villager.e(trader) <= 16 && trader.activeContainer != null;
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
		this.m_villager.a((EntityHuman)null);
	}
}
