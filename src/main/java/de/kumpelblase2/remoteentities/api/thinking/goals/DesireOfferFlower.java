package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R2.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.*;

/**
 * Using this desire the entity will offer the nearest entity of the given type a flower.
 * You can also specify the entity type.
 * Keep in mind that this is designed for iron golems and might not work properly on other entities.
 */
public class DesireOfferFlower extends DesireBase
{
	protected int m_offerTick;
	protected EntityLiving m_nearestEntity;
	@SerializeAs(pos = 1)
	protected Class<? extends Entity> m_toOffer;

	@Deprecated
	public DesireOfferFlower(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_toOffer = EntityVillager.class;
		this.m_type = DesireType.SUBCONSCIOUS;
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public DesireOfferFlower(RemoteEntity inEntity, Class<?> inToOffer)
	{
		this(inEntity);
		if(Entity.class.isAssignableFrom(inToOffer))
			this.m_toOffer = (Class<? extends Entity>)inToOffer;
		else
			this.m_toOffer = (Class<? extends Entity>)NMSClassMap.getNMSClass(inToOffer);
	}

	public DesireOfferFlower()
	{
		super();
		this.m_toOffer = EntityVillager.class;
		this.m_type = DesireType.SUBCONSCIOUS;
	}

	@SuppressWarnings("unchecked")
	public DesireOfferFlower(Class<?> inToOffer)
	{
		this();
		if(Entity.class.isAssignableFrom(inToOffer))
			this.m_toOffer = (Class<? extends Entity>)inToOffer;
		else
			this.m_toOffer = (Class<? extends Entity>)NMSClassMap.getNMSClass(inToOffer);
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null || this.getEntityHandle().world.v())
			return false;
		else if(this.getEntityHandle().aC().nextInt(8000) != 0)
			return false;
		else
		{
			this.m_nearestEntity = (EntityLiving)this.getEntityHandle().world.a(this.m_toOffer, this.getEntityHandle().boundingBox.grow(6, 2, 6), this.getEntityHandle());
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
		NMSUtil.getControllerLook(this.getEntityHandle()).a(this.m_nearestEntity, 30, 30);
		this.m_offerTick--;
		return true;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}