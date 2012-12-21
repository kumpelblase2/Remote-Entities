package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.entity.LivingEntity;
import net.minecraft.server.v1_4_6.EntityVillager;
import net.minecraft.server.v1_4_6.MathHelper;
import net.minecraft.server.v1_4_6.Village;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.NotAVillagerException;

public class DesireMakeLove extends DesireBase
{
	protected EntityVillager m_villager;
	protected EntityVillager m_partner;
	protected Village m_nearestVillage;
	protected int m_loveTick;
	
	public DesireMakeLove(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityVillager))
			throw new NotAVillagerException();
		
		this.m_villager = (EntityVillager)this.getEntityHandle();
		this.m_type = 3;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.m_villager.getAge() != 0)
			return false;
		else if(this.m_villager.aB().nextInt(500) != 0)
			return false;
		else
		{
			this.m_nearestVillage = this.m_villager.world.villages.getClosestVillage(MathHelper.floor(this.m_villager.locX), MathHelper.floor(this.m_villager.locY), MathHelper.floor(this.m_villager.locZ), 0);
			if(this.m_nearestVillage == null)
				return false;
			else if(!this.hasEnoughSpace())
				return false;
			else
			{
				EntityVillager partner = (EntityVillager)this.m_villager.world.a(EntityVillager.class, this.m_villager.boundingBox.grow(8, 3, 8));
				if(partner == null)
					return false;
				else
				{
					this.m_partner = partner;
					return this.m_partner.getAge() == 0;
				}
			}
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.m_loveTick = 300;
		this.m_villager.e(true);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_nearestVillage = null;
		this.m_partner = null;
		this.m_villager.e(false);
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_loveTick >= 0 && this.hasEnoughSpace() && this.m_villager.getAge() == 0;
	}
	
	@Override
	public boolean update()
	{
		this.m_loveTick--;
		this.m_villager.getControllerLook().a(this.m_partner, 10, 30);
		if(this.m_villager.e(this.m_partner) > 2.25)
			this.getRemoteEntity().move((LivingEntity)this.m_partner);
		else if(this.m_loveTick == 0 && this.m_partner.o())
			this.createBaby();
		
		if(this.m_villager.aB().nextInt(35) == 0)
			this.m_villager.world.broadcastEntityEffect(this.m_villager, (byte)12);
		
		return true;
	}
	
	protected void createBaby()
	{
		EntityVillager baby = new EntityVillager(this.m_villager.world);
		this.m_villager.setAge(6000);
		this.m_villager.setAge(6000);
		baby.setAge(-24000);
		baby.setProfession(this.m_villager.aB().nextInt(5));
		baby.setPositionRotation(this.m_villager.locX, this.m_villager.locY, this.m_villager.locZ, 0, 0);
		this.m_villager.world.addEntity(baby);
		this.m_villager.world.broadcastEntityEffect(baby, (byte)12);
	}
	
	protected boolean hasEnoughSpace()
	{
		int i = (int)(this.m_nearestVillage.getDoorCount() * 0.35D);
		return this.m_nearestVillage.getPopulationCount() < i; 
	}
}
