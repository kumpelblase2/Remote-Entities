package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import net.minecraft.server.v1_5_R1.EntityLiving;
import net.minecraft.server.v1_5_R1.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;

public abstract class DesireTamedBase extends DesireTargetBase
{
	protected EntityLiving m_animal;

	public DesireTamedBase(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight)
	{
		super(inEntity, inDistance, inShouldCheckSight);
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
