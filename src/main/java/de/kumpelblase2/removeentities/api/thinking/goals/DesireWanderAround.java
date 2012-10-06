package de.kumpelblase2.removeentities.api.thinking.goals;

import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.Vec3D;
import de.kumpelblase2.removeentities.api.RemoteEntity;
import de.kumpelblase2.removeentities.api.thinking.Desire;
import de.kumpelblase2.removeentities.nms.RandomPositionGenerator;

public class DesireWanderAround extends PathfinderGoal implements Desire
{
	private RemoteEntity m_entity;
	protected double m_xPos;
	protected double m_yPos;
	protected double m_zPos;
	protected float m_speed;
	
	public DesireWanderAround(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_speed = inEntity.getSpeed();
		this.a(1);
	}
	
	@Override
	public boolean a()
	{
		if(this.m_entity.getHandle().ax() >= 100)
		{
			return false;
		}
		else if(this.m_entity.getHandle().au().nextInt(120) != 0)
		{
			return false;
		}
		else
		{
			Vec3D vector = RandomPositionGenerator.a(this.m_entity.getHandle(), 10, 7);
			if(vector == null)
			{
				return false;
			}
			else
			{
				this.m_xPos = vector.a;
				this.m_yPos = vector.b;
				this.m_zPos = vector.c;
				return true;
			}
		}
	}
	
	@Override
	public boolean b()
	{
		return !this.m_entity.getHandle().getNavigation().f();
	}
	
	@Override
	public void e()
	{
		this.m_entity.getHandle().getNavigation().a(this.m_xPos, this.m_yPos, this.m_zPos, this.m_speed);
	}
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
}
