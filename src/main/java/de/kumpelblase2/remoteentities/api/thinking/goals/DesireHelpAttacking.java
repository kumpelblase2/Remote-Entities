package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.exceptions.NotTameableException;

public class DesireHelpAttacking extends DesireTargetBase
{
	protected EntityLiving m_animal;
	protected EntityLiving m_ownerTarget;
	
	public DesireHelpAttacking(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight) throws Exception
	{
		super(inEntity, inDistance, inShouldCheckSight);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal) && !this.getRemoteEntity().getFeatures().hasFeature(TamingFeature.class))
			throw new NotTameableException();
		
		this.m_animal = this.getEntityHandle();
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_animal == null)
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
				this.m_ownerTarget = owner.aD();
				return this.isSuitableTarget(this.m_ownerTarget, false);
			}
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.getEntityHandle().b(this.m_ownerTarget);
		super.startExecuting();
	}
	
	protected boolean isTamed()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).isTamed();
		else
			return this.getRemoteEntity().getFeatures().getFeature(TamingFeature.class).isTamed();
	}
	
	protected EntityLiving getTamer()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).getOwner();
		else
			return ((CraftPlayer)this.getRemoteEntity().getFeatures().getFeature(TamingFeature.class).getTamer()).getHandle();
	}
}
