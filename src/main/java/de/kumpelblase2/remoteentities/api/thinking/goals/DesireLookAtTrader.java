package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_6.EntityHuman;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.EntityVillager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireLookAtTrader extends DesireLookAtNearest
{
	public DesireLookAtTrader(RemoteEntity inEntity, float inMinDistance)
	{
		super(inEntity, EntityHuman.class, inMinDistance);
	}
	
	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(!(entity instanceof EntityVillager))
			return false;
		else
		{
			EntityVillager villager = (EntityVillager)entity;
			if(villager.p())
			{
				this.m_target = villager.m_();
				return true;
			}
			return false;
		}
	}
}
