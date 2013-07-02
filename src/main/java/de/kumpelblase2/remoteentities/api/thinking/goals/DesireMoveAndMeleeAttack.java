package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.craftbukkit.v1_6_R1.event.CraftEventFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.*;

/**
 * This desire moves towards the entities target and deals damage when it's in melee range of the entity.
 */
public class DesireMoveAndMeleeAttack extends DesireBase
{
	@SerializeAs(pos = 1)
	protected Class<? extends Entity> m_toAttack;
	protected EntityLiving m_target;
	protected int m_attackTick;
	protected int m_moveTick;
	protected PathEntity m_path;
	@SerializeAs(pos = 2)
	protected boolean m_ignoreSight;
	@SerializeAs(pos = 3)
	protected float m_speed;

	public DesireMoveAndMeleeAttack(RemoteEntity inEntity, Class<?> inToAttack, boolean inIgnoreSight)
	{
		this(inEntity, inToAttack, inIgnoreSight, inEntity.getSpeed());
	}

	@SuppressWarnings("unchecked")
	public DesireMoveAndMeleeAttack(RemoteEntity inEntity, Class<?> inToAttack, boolean inIgnoreSight, float inSpeed)
	{
		super(inEntity);
		this.m_speed = inSpeed;
		this.m_ignoreSight = inIgnoreSight;
		if(inToAttack == null)
			this.m_toAttack = Entity.class;
		else if(Entity.class.isAssignableFrom(inToAttack))
			this.m_toAttack = (Class<? extends Entity>)inToAttack;
		else
			this.m_toAttack = (Class<? extends Entity>)NMSClassMap.getNMSClass(inToAttack);

		this.m_attackTick = 0;
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		EntityLiving entityTarget = NMSUtil.getGoalTarget(this.getEntityHandle());

		if(entityTarget == null)
			return false;
		else if(this.m_toAttack != null && !this.m_toAttack.isAssignableFrom(entityTarget.getClass()))
			return false;
		else
		{
			this.m_target = entityTarget;
			this.m_path = NMSUtil.getNavigation(this.getEntityHandle()).a(this.m_target);
			return this.m_path != null;
		}
	}

	@Override
	public boolean canContinue()
	{
		EntityLiving entityTarget = NMSUtil.getGoalTarget(this.getEntityHandle());
		EntityLiving entity = this.getEntityHandle();
		if(entityTarget == null)
			return false;

		if(!this.m_target.isAlive())
			return false;

		if(!this.m_ignoreSight)
			return !NMSUtil.getNavigation(entity).g();
		else
			return entity.b(MathHelper.floor(this.m_target.locX), MathHelper.floor(this.m_target.locY), MathHelper.floor(this.m_target.locZ)); //TODO does b() only exist in creature?
	}

	@Override
	public void startExecuting()
	{
		this.movePath(this.m_path, this.m_speed);
		this.m_moveTick = 0;
	}

	@Override
	public void stopExecuting()
	{
		EntityTargetEvent.TargetReason reason = this.m_target.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
        CraftEventFactory.callEntityTargetEvent(this.getEntityHandle(), null, reason);

		this.m_target = null;
		NMSUtil.getNavigation(this.getEntityHandle()).h();
	}

	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		NMSUtil.getControllerLook(entity).a(this.m_target, 30, 30);
		if((this.m_ignoreSight || NMSUtil.getEntitySenses(entity).canSee(this.m_target)) && --this.m_moveTick <= 0)
		{
			this.m_moveTick = 4 + entity.aB().nextInt(7);
			this.getRemoteEntity().move((LivingEntity)this.m_target.getBukkitEntity(), this.m_speed);
		}

		this.m_attackTick = Math.max(this.m_attackTick - 1, 0);
		double minDist = entity.width * 2 * entity.width * 2;
		if(this.m_attackTick <= 0 && entity.e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ) <= minDist)
		{
			this.m_attackTick = 20;
			if(entity.aV() != null)
				this.getEntityHandle().aR();

			this.attack(this.m_target.getBukkitEntity());
		}
		return true;
	}

	public void attack(Entity inEntity)
	{
		this.getEntityHandle().m(this.m_target);
	}

	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}