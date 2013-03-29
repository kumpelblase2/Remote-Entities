package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.exceptions.NotAnAnimalException;

public class DesireSit extends DesireBase
{
	protected EntityTameableAnimal m_animal;
	protected boolean m_canSit = false;
	
	public DesireSit(RemoteEntity inEntity) throws Exception
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal))
			throw new NotAnAnimalException();
		
		this.m_animal = (EntityTameableAnimal)this.getEntityHandle();
		this.m_type = 5;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_animal == null)
			return false;
		
		if(!this.m_animal.isTamed())
			return false;
		else if(this.m_animal.H())
			return false;
		else if(!this.m_animal.onGround)
			return false;
		else
		{
			EntityLiving owner = this.m_animal.getOwner();
			return owner == null ? true : (this.m_animal.e(owner) < 144 && owner.getGoalTarget() != null ? false : this.m_canSit);
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.m_animal.getNavigation().g();
		this.m_animal.setSitting(true);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_animal.setSitting(false);
	}
	
	public void canSit(boolean inFlag)
	{
		this.m_canSit = inFlag;
	}
}
