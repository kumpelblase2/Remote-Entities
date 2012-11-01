package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.exceptions.NotTameableException;

public class DesireProtectOwner extends DesireTargetBase
{
	protected EntityLiving m_animal;
	protected EntityLiving m_ownerTarget;
	
	public DesireProtectOwner(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight) throws Exception
	{
		super(inEntity, inDistance, inShouldCheckSight);
		if(!(this.getRemoteEntity().getHandle() instanceof EntityTameableAnimal) && !this.getRemoteEntity().getFeatures().hasFeature("TAMING"))
			throw new NotTameableException();
		
		this.m_animal = this.getRemoteEntity().getHandle();
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(!this.isTamed())
			return false;
		else
		{
			EntityLiving owner = this.getTamer();
			if(owner == null)
				return false;
			else
			{
				this.m_ownerTarget = owner.av();
				return this.isSuitableTarget(this.m_ownerTarget, false);
			}
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().b(this.m_ownerTarget);
		super.startExecuting();
	}
	
	protected boolean isTamed()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).isTamed();
		else
			return ((TamingFeature)this.getRemoteEntity().getFeatures().getFeature("TAMING")).isTamed();
	}
	
	protected EntityLiving getTamer()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).getOwner();
		else
			return ((CraftPlayer)((TamingFeature)this.getRemoteEntity().getFeatures().getFeature("TAMING")).getTamer()).getHandle();
	}
}
