package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.entity.LivingEntity;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.EntityVillager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireLookAtTrader extends DesireLookAtNearest
{
	public DesireLookAtTrader(RemoteEntity inEntity, float inMinDistance)
	{
		super(inEntity, EntityHuman.class, inMinDistance);
	}
	
	@Deprecated
	public DesireLookAtTrader(RemoteEntity inEntity, Class<? extends LivingEntity> inTarget, float inMinDistance)
	{
		this(inEntity, inMinDistance);
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
