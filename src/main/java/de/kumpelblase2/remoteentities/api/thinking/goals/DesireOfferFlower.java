package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityVillager;
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
		if(this.getRemoteEntity().getHandle().world.s())
			return false;
		else if(this.getRemoteEntity().getHandle().au().nextInt(8000) != 0)
			return false;
		else
		{
			this.m_nearestEntity = (EntityLiving)this.getRemoteEntity().getHandle().world.a(this.m_toOfffer, this.getRemoteEntity().getHandle().boundingBox.grow(6, 2, 6), this.getRemoteEntity().getHandle());
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
		if(this.getRemoteEntity().getHandle() instanceof EntityIronGolem)
			((EntityIronGolem)this.getRemoteEntity().getHandle()).e(true);
		else
			this.getRemoteEntity().getHandle().world.broadcastEntityEffect(this.getRemoteEntity().getHandle(), (byte)11);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_nearestEntity = null;
		if(this.getRemoteEntity().getHandle() instanceof EntityIronGolem)
			((EntityIronGolem)this.getRemoteEntity().getHandle()).e(false);
		else
			this.getRemoteEntity().getHandle().world.broadcastEntityEffect(this.getRemoteEntity().getHandle(), (byte)11);
	}
	
	@Override
	public boolean update()
	{
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_nearestEntity, 30, 30);
		this.m_offerTick--;
		return true;
	}
}
