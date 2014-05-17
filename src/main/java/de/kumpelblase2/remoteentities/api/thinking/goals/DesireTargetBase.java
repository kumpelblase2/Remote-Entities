package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public abstract class DesireTargetBase extends DesireBase
{
	@SerializeAs(pos = 2)
	protected boolean m_shouldCheckSight;
	@SerializeAs(pos = 3)
	protected boolean m_shouldMeleeAttack;
	@SerializeAs(pos = 1)
	protected float m_distance;
	protected float m_distanceSquared;
	protected int m_useAttack;
	protected int m_lastAttackTick;
	protected int m_notSeeingTarget;

	@Deprecated
	public DesireTargetBase(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight)
	{
		this(inEntity, inDistance, inShouldCheckSight, false);
	}

	@Deprecated
	public DesireTargetBase(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inShouldMelee)
	{
		super(inEntity);
		this.m_shouldCheckSight = inShouldCheckSight;
		this.m_distance = inDistance;
		this.m_distanceSquared = this.m_distance * this.m_distance;
		this.m_shouldMeleeAttack = inShouldMelee;
		this.m_useAttack = 0;
		this.m_lastAttackTick = 0;
		this.m_notSeeingTarget = 0;
	}

	public DesireTargetBase(float inDistance, boolean inShouldCheckSight)
	{
		this(inDistance, inShouldCheckSight, false);
	}

	public DesireTargetBase(float inDistance, boolean inShouldCheckSight, boolean inShouldMelee)
	{
		super();
		this.m_shouldCheckSight = inShouldCheckSight;
		this.m_distance = inDistance;
		this.m_distanceSquared = this.m_distance * this.m_distance;
		this.m_shouldMeleeAttack = inShouldMelee;
		this.m_useAttack = 0;
		this.m_lastAttackTick = 0;
		this.m_notSeeingTarget = 0;
	}

	@Override
	public void startExecuting()
	{
		this.m_useAttack = 0;
		this.m_lastAttackTick = 0;
		this.m_notSeeingTarget = 0;
	}

	@Override
	public void stopExecuting()
	{
		NMSUtil.setGoalTarget(this.getEntityHandle(), null);
	}

	@Override
	public boolean canContinue()
	{
		EntityLiving target = NMSUtil.getGoalTarget(this.getEntityHandle());

		if(target == null)
			return false;
		else if(!target.isAlive())
			return false;
		else if(this.getEntityHandle().e(target) > (this.m_distanceSquared))
			return false;
		else
		{
			if(this.m_shouldCheckSight)
			{
				if(NMSUtil.getEntitySenses(this.getEntityHandle()).canSee(target))
					this.m_notSeeingTarget = 0;
				else if(++this.m_notSeeingTarget > 60)
					return false;
			}
			return true;
		}
	}

	protected boolean isSuitableTarget(EntityLiving inEntity, boolean inAttackInvulnerablePlayer)
	{
		if(inEntity == null)
			return false;
		else if(inEntity == this.getEntityHandle())
			return false;
		else if(!inEntity.isAlive())
			return false;
		else if(!this.canAttackClass(inEntity.getClass()))
			return false;
		else
		{
			if(this.getEntityHandle() instanceof EntityTameableAnimal && ((EntityTameableAnimal)this.getEntityHandle()).isTamed())
			{
				if(inEntity instanceof EntityTameableAnimal && ((EntityTameableAnimal)inEntity).isTamed())
					return false;

				if(inEntity == ((EntityTameableAnimal)this.getEntityHandle()).getOwner())
					return false;
			}
			else if(this.m_entity.getFeatures().hasFeature(TamingFeature.class) && this.m_entity.getFeatures().getFeature(TamingFeature.class).isTamed())
			{
				if(inEntity instanceof EntityTameableAnimal && ((EntityTameableAnimal)inEntity).isTamed())
					return false;

				if(inEntity == ((CraftPlayer)this.m_entity.getFeatures().getFeature(TamingFeature.class).getTamer()).getHandle())
					return false;
			}
			else if(inEntity instanceof EntityHuman && !inAttackInvulnerablePlayer && ((EntityHuman)inEntity).abilities.isInvulnerable)
				return false;

			if(!NMSUtil.isInHomeArea(this.getEntityHandle(), MathHelper.floor(inEntity.locX), MathHelper.floor(inEntity.locY), MathHelper.floor(inEntity.locZ)))
				return false;
			else if(this.m_shouldCheckSight && !NMSUtil.getEntitySenses(this.getEntityHandle()).canSee(inEntity))
				return false;
			else
			{
				if(this.m_shouldMeleeAttack)
				{
					if(--this.m_lastAttackTick <= 0)
						this.m_useAttack = 0;

					if(this.m_useAttack == 0)
						this.m_useAttack = this.useAttack(inEntity) ? 1 : 2;

					if(this.m_useAttack == 2)
						return false;
				}

				EntityTargetEvent.TargetReason reason = EntityTargetEvent.TargetReason.RANDOM_TARGET;

				if(this instanceof DesireDefendVillage)
					reason = EntityTargetEvent.TargetReason.DEFEND_VILLAGE;
				else if(this instanceof DesireFindAttackingTarget)
					reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY;
				else if(this instanceof DesireFindNearestTarget)
				{
					if(inEntity instanceof EntityHuman)
						reason = EntityTargetEvent.TargetReason.CLOSEST_PLAYER;
				}
				else if(this instanceof DesireProtectOwner)
					reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER;
				else if(this instanceof DesireHelpAttacking)
					reason = EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET;

				EntityTargetLivingEntityEvent event = CraftEventFactory.callEntityTargetLivingEvent(this.getEntityHandle(), inEntity, reason);
				if(event.isCancelled() || event.getTarget() == null)
				{
					if(this.getEntityHandle() instanceof EntityCreature)
						NMSUtil.setGoalTarget(this.getEntityHandle(), null);

					return false;
				}
				else if(inEntity.getBukkitEntity() != event.getTarget())
					NMSUtil.setGoalTarget(this.getEntityHandle(), (EntityLiving)((CraftEntity)event.getTarget()).getHandle());

				if(this.getEntityHandle() instanceof EntityCreature)
					((EntityCreature)this.getEntityHandle()).target = ((CraftEntity)event.getTarget()).getHandle();

				return true;
			}
		}
	}

	protected boolean useAttack(EntityLiving inEntity)
	{
		this.m_lastAttackTick = 10 + this.getEntityHandle().aH().nextInt(5);
		PathEntity path = this.getNavigation().a(inEntity);

		if(path == null)
			return false;
		else
		{
			PathPoint lastPoint = path.c();

			if(lastPoint == null)
				return false;
			else
			{
				int distX = lastPoint.a - MathHelper.floor(inEntity.locX);
				int distY = lastPoint.c - MathHelper.floor(inEntity.locZ);

				return (distX * distX + distY * distY) <= 2.25D;
			}
		}
	}

	protected boolean canAttackClass(Class inClass)
	{
		if(this.getEntityHandle() instanceof EntityInsentient)
			return ((EntityInsentient)this.getEntityHandle()).a(inClass);
		else
			return inClass != EntityGhast.class;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
