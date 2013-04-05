package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.EntityIronGolem;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.EntityVillager;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSClassMap;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class DesireOfferFlower extends DesireBase
{
	protected int m_offerTick;
	protected EntityLiving m_nearestEntity;
	@SerializeAs(pos = 1)
	protected Class<? extends Entity> m_toOfffer;
	
	public DesireOfferFlower(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_toOfffer = EntityVillager.class;
		this.m_type = DesireType.SUBCONSCIOUS;
	}
	
	@SuppressWarnings("unchecked")
	public DesireOfferFlower(RemoteEntity inEntity, Class<?> inToOffer)
	{
		this(inEntity);
		if(Entity.class.isAssignableFrom(inToOffer))
			this.m_toOfffer = (Class<? extends Entity>)inToOffer;
		else
			this.m_toOfffer = (Class<? extends Entity>)NMSClassMap.getNMSClass(inToOffer);
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null || this.getEntityHandle().world.u())
			return false;
		else if(this.getEntityHandle().aE().nextInt(8000) != 0)
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
			((EntityIronGolem)this.getEntityHandle()).a(true);
		else
			this.getEntityHandle().world.broadcastEntityEffect(this.getEntityHandle(), (byte)11);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_nearestEntity = null;
		if(this.getEntityHandle() instanceof EntityIronGolem)
			((EntityIronGolem)this.getEntityHandle()).a(false);
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
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
