package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_R1.EntityCreeper;
import net.minecraft.server.v1_4_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.NotACreeperException;

public class DesireSwell extends DesireBase
{
	protected EntityCreeper m_creeper;
	protected EntityLiving m_target;
	
	public DesireSwell(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityCreeper))
			throw new NotACreeperException();
		
		this.m_creeper = (EntityCreeper)this.getEntityHandle();
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_creeper == null)
			return false;
		
		EntityLiving target = this.m_creeper.getGoalTarget();
		return this.m_creeper.o() > 0 || target != null && this.m_creeper.e(target) < 9;
	}
	
	@Override
	public void startExecuting()
	{
		this.m_creeper.getNavigation().g();
		this.m_target = this.m_creeper.getGoalTarget();
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}
	
	@Override
	public boolean update()
	{
		if(this.m_target == null)
			this.m_creeper.a(-1);
		else if(this.m_creeper.e(this.m_target) > 49)
			this.m_creeper.a(-1);
		else if(!this.m_creeper.aA().canSee(this.m_target))
			this.m_creeper.a(-1);
		else
			this.m_creeper.a(1);
		return true;
	}
}
