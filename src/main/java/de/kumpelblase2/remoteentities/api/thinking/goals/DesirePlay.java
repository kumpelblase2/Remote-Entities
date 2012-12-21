package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import net.minecraft.server.v1_4_6.EntityVillager;
import net.minecraft.server.v1_4_6.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.NotAVillagerException;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;

public class DesirePlay extends DesireBase
{
	protected EntityVillager m_villager;
	protected EntityVillager m_friend;
	protected int m_playTick;
	
	public DesirePlay(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityVillager))
			throw new NotAVillagerException();
		
		this.m_villager = (EntityVillager)this.getEntityHandle();
		this.m_type = 1;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.m_villager.getAge() >= 0)
			return false;
		else if(this.m_villager.aB().nextInt(400) != 0)
			return false;
		else
		{
			List villagers = this.m_villager.world.a(EntityVillager.class, this.m_villager.boundingBox.grow(6, 3, 6));
			double minDist = Double.MAX_VALUE;
			Iterator it = villagers.iterator();
			
			while(it.hasNext())
			{
				EntityVillager villager = (EntityVillager)it.next();
				if(villager != this.m_villager && !villager.p() && villager.getAge() < 0)
				{
					double dist = villager.e(this.m_villager);
					
					if(dist <= minDist)
					{
						minDist = dist;
						this.m_friend = villager;
					}
				}
			}
			
			if(this.m_friend == null)
			{
				Vec3D vec = RandomPositionGenerator.a(this.m_villager, 16, 3);
				
				if(vec == null)
					return false;
			}
			
			return true;
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_playTick > 0;
	}
	
	@Override
	public void startExecuting()
	{
		if(this.m_friend != null)
			this.m_villager.f(true);
		
		this.m_playTick = 1000;
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_villager.f(false);
		this.m_friend = null;
	}
	
	@Override
	public boolean update()
	{
		this.m_playTick--;
		if(this.m_friend != null)
		{
			if(this.m_villager.e(this.m_friend) > 4)
				this.getRemoteEntity().move((LivingEntity)this.m_friend.getBukkitEntity());
		}
		else if(this.m_villager.getNavigation().f())
		{
			Vec3D vec = RandomPositionGenerator.a(this.m_villager, 16, 3);
			
			if(vec == null)
				return true;
			
			this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), vec.c, vec.d, vec.e));
		}
		return true;
	}
}
