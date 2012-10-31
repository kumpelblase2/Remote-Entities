package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;

public class DesireWanderAround extends DesireBase
{
	protected double m_xPos;
	protected double m_yPos;
	protected double m_zPos;
	
	public DesireWanderAround(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 1;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if(this.getRemoteEntity().getHandle().ax() >= 100)
		{
			return false;
		}
		else if(this.getRemoteEntity().getHandle().au().nextInt(120) != 0)
		{
			return false;
		}
		else
		{
			Vec3D vector = RandomPositionGenerator.a(this.getRemoteEntity().getHandle(), 10, 7);
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
	public boolean canContinue()
	{
		return !this.getRemoteEntity().getHandle().getNavigation().f();
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().a(this.m_xPos, this.m_yPos, this.m_zPos, this.getRemoteEntity().getSpeed());
	}
}
