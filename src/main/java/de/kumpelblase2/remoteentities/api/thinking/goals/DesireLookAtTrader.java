package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R1.Entity;
import net.minecraft.server.v1_5_R1.EntityHuman;
import net.minecraft.server.v1_5_R1.EntityLiving;
import net.minecraft.server.v1_5_R1.EntityVillager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.utilities.NMSClassMap;

public class DesireLookAtTrader extends DesireLookAtNearest
{
	public DesireLookAtTrader(RemoteEntity inEntity, float inMinDistance)
	{
		super(inEntity, EntityHuman.class, inMinDistance);
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public DesireLookAtTrader(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance)
	{
		this(inEntity, inMinDistance);
		if(Entity.class.isAssignableFrom(inTarget))
			this.m_toLookAt = (Class<? extends Entity>)inTarget;
		else
			this.m_toLookAt = (Class<? extends Entity>)NMSClassMap.getNMSClass(inTarget);
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
