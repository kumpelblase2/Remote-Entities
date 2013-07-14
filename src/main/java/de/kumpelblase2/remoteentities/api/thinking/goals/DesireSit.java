package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R2.EntityLiving;
import net.minecraft.server.v1_6_R2.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.exceptions.NotAnAnimalException;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * Using this desire the animal will sit when the tamer says so.
 */
public class DesireSit extends DesireBase
{
	protected EntityTameableAnimal m_animal;
	protected boolean m_canSit = false;

	@Deprecated
	public DesireSit(RemoteEntity inEntity)
	{
		super(inEntity);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal))
			throw new NotAnAnimalException();

		this.m_animal = (EntityTameableAnimal)this.getEntityHandle();
		this.m_type = DesireType.OCCASIONAL_URGE;
	}

	public DesireSit()
	{
		super();
		this.m_type = DesireType.OCCASIONAL_URGE;
	}

	@Override
	public void onAdd(RemoteEntity inEntity)
	{
		super.onAdd(inEntity);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal))
			throw new NotAnAnimalException();

		this.m_animal = (EntityTameableAnimal)this.getEntityHandle();
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_animal == null)
			return false;

		if(!this.m_animal.isTamed())
			return false;
		else if(this.m_animal.G())
			return false;
		else if(!this.m_animal.onGround)
			return false;
		else
		{
			EntityLiving owner = this.m_animal.getOwner();
			if(owner == null)
				return true;

			return !(this.m_animal.e(owner) < 144 && NMSUtil.getGoalTarget(owner) != null) && this.m_canSit;
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
