package de.kumpelblase2.remoteentities.api.features;

import net.minecraft.server.v1_6_R2.EntityAnimal;
import net.minecraft.server.v1_6_R2.EntityLiving;
import org.bukkit.entity.LivingEntity;

public abstract class RemoteMateFeature extends RemoteFeature implements MateFeature
{
	private LivingEntity m_partner;

	public RemoteMateFeature()
	{
		super("MATE");
	}

	@Override
	public boolean isPossiblePartner(LivingEntity inPartner)
	{
		return true;
	}

	@Override
	public boolean mate(LivingEntity inPartner)
	{
		if(this.isPossiblePartner(inPartner))
		{
			this.m_partner = inPartner;
			return true;
		}

		return false;
	}

	@Override
	public boolean isAffected()
	{
		EntityLiving handle = this.m_entity.getHandle();
		return !(handle instanceof EntityAnimal) || ((EntityAnimal)handle).bY();
	}

	@Override
	public void resetAffection()
	{
		if(this.m_entity.getHandle() instanceof EntityAnimal)
			((EntityAnimal)this.m_entity.getHandle()).bZ();
	}
}