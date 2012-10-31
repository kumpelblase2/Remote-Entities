package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireLookRandomly extends DesireBase
{
	protected double m_xDiff;
	protected double m_zDiff;
	protected int m_lookTick;
	
	public DesireLookRandomly(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 3;
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getRemoteEntity().getHandle().au().nextFloat() < 0.02F;
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_lookTick >= 0;
	}
	
	@Override
	public void startExecuting()
	{
		double d = 6.283185307179586D * this.getRemoteEntity().getHandle().au().nextDouble();
		
		this.m_xDiff = Math.cos(d);
		this.m_zDiff = Math.sin(d);
		this.m_lookTick = 20 + this.getRemoteEntity().getHandle().au().nextInt(20);
	}
	
	@Override
	public boolean update()
	{
		this.m_lookTick--;
		EntityLiving entity = this.getRemoteEntity().getHandle();
		entity.getControllerLook().a(entity.locX + this.m_xDiff, entity.locY + entity.getHeadHeight(), entity.locZ + this.m_zDiff, 10, entity.bf());
		return true;
	}
}
