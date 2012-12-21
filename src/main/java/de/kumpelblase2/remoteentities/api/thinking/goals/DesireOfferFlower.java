package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_6.EntityIronGolem;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.EntityVillager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireOfferFlower extends DesireBase
{
	protected int m_offerTick;
	protected EntityLiving m_nearestEntity;
	protected Class<? extends EntityLiving> m_toOfffer;
	
	public DesireOfferFlower(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_toOfffer = EntityVillager.class;
		this.m_type = 3;
	}
	
	public DesireOfferFlower(RemoteEntity inEntity, Class<? extends EntityLiving> inToOffer)
	{
		this(inEntity);
		this.m_toOfffer = inToOffer;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null || this.getEntityHandle().world.u())
			return false;
		else if(this.getEntityHandle().aB().nextInt(8000) != 0)
			return false;
		else
		{
			this.m_nearestEntity = (EntityLiving)this.getEntityHandle().world.a(this.m_toOfffer, this.getEntityHandle().boundingBox.grow(6, 2, 6), this.getEntityHandle());
			return this.m_nearestEntity != null;
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_offerTick > 0;
	}
	
	@Override
	public void startExecuting()
	{
		this.m_offerTick = 400;
		if(this.getEntityHandle() instanceof EntityIronGolem)
			((EntityIronGolem)this.getEntityHandle()).e(true);
		else
			this.getEntityHandle().world.broadcastEntityEffect(this.getEntityHandle(), (byte)11);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_nearestEntity = null;
		if(this.getEntityHandle() instanceof EntityIronGolem)
			((EntityIronGolem)this.getEntityHandle()).e(false);
		else
			this.getEntityHandle().world.broadcastEntityEffect(this.getEntityHandle(), (byte)11);
	}
	
	@Override
	public boolean update()
	{
		this.getEntityHandle().getControllerLook().a(this.m_nearestEntity, 30, 30);
		this.m_offerTick--;
		return true;
	}
}
