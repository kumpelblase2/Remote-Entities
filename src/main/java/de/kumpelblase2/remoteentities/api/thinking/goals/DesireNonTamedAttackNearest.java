package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireNonTamedAttackNearest extends DesireAttackNearest
{
	protected EntityTameableAnimal m_animal;

	public DesireNonTamedAttackNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele, int inChance) throws Exception
	{
		super(inEntity, inTargetClass, inDistance, inShouldCheckSight, inShouldMeele, inChance);
		if(!(this.getRemoteEntity().getHandle() instanceof EntityTameableAnimal))
			throw new Exception("Entity needs to be a tamed entity");
		
		this.m_animal = (EntityTameableAnimal)this.getRemoteEntity().getHandle();
	}
	
	@Override
	public boolean shouldExecute()
	{
		return this.m_animal.isTamed() ? false : super.shouldExecute();
	}
}
