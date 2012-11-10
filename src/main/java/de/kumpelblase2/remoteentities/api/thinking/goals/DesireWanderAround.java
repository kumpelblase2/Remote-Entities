package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Location;
import net.minecraft.server.EntityTameableAnimal;
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
		if(this.getRemoteEntity().getHandle().aD() >= 100)
			return false;
		else if(this.getRemoteEntity().getHandle().aA().nextInt(120) != 0)
			return false;
		else if(this.getRemoteEntity().getHandle() instanceof EntityTameableAnimal && ((EntityTameableAnimal)this.getRemoteEntity().getHandle()).isSitting())
			return false;
		else
		{
			Vec3D vector = RandomPositionGenerator.a(this.getRemoteEntity().getHandle(), 10, 7);
			if(vector == null)
				return false;
			else
			{
				this.m_xPos = vector.c;
				this.m_yPos = vector.d;
				this.m_zPos = vector.e;
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
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_xPos, this.m_yPos, this.m_zPos));
	}
}
