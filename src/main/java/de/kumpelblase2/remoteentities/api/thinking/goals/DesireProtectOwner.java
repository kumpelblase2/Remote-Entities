package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.exceptions.NotTameableException;

/**
 * Using this desire the entity will target the entity which is attacking the tamer of this entity.
 * However, this desire will not attack the target directly.
 */
public class DesireProtectOwner extends DesireTamedBase
{
	protected EntityLiving m_ownerAttacker;

	public DesireProtectOwner(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight)
	{
		super(inEntity, inDistance, inShouldCheckSight);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal) && !this.getRemoteEntity().getFeatures().hasFeature(TamingFeature.class))
			throw new NotTameableException();

		this.m_animal = this.getEntityHandle();
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		if(!this.isTamed())
			return false;
		else
		{
			EntityLiving owner = this.getTamer();
			if(owner == null)
				return false;
			else
			{
				this.m_ownerAttacker = owner.aF();
				return this.isSuitableTarget(this.m_ownerAttacker, false);
			}
		}
	}

	@Override
	public void startExecuting()
	{
		this.getEntityHandle().setGoalTarget(this.m_ownerAttacker);
		super.startExecuting();
	}
}