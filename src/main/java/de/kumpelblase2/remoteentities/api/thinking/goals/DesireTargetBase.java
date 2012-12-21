package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.server.v1_4_6.EntityCreature;
import net.minecraft.server.v1_4_6.EntityHuman;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.EntityTameableAnimal;
import net.minecraft.server.v1_4_6.MathHelper;
import net.minecraft.server.v1_4_6.PathEntity;
import net.minecraft.server.v1_4_6.PathPoint;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public abstract class DesireTargetBase extends DesireBase
{
	protected boolean m_shouldCheckSight;
	protected boolean m_shouldMeleeAttack;
	protected float m_distance;
	protected int m_useAttack;
	protected int m_lastAttackTick;
	protected int m_notSeeingTarget;
	
	public DesireTargetBase(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight)
	{
		this(inEntity, inDistance, inShouldCheckSight, false);
	}
	
	public DesireTargetBase(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele)
	{
		super(inEntity);
		this.m_shouldCheckSight = inShouldCheckSight;
		this.m_distance = inDistance;
		this.m_shouldMeleeAttack = inShouldMeele;
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
		this.getEntityHandle().b((EntityLiving)null);
	}

	@Override
	public boolean canContinue()
	{
		EntityLiving target = this.getEntityHandle().aG();
		
		if(target == null)
			return false;
		else if(!target.isAlive())
			return false;
		else if(this.getEntityHandle().e(target) > (this.m_distance * this.m_distance))
			return false;
		else
		{
			if(this.m_shouldCheckSight)
			{
				if(this.getEntityHandle().aA().canSee(target))
					this.m_notSeeingTarget = 0;
				else if(++this.m_notSeeingTarget > 60)
					return false;
			}
			return true;
		}
	}
	
	protected boolean isSuitableTarget(EntityLiving inEntity, boolean inAttackInvulnurablePlayer)
	{
		if(inEntity == null)
			return false;
		else if(inEntity == this.getEntityHandle())
			return false;
		else if(!inEntity.isAlive())
			return false;
		else if(inEntity.boundingBox.e > this.getEntityHandle().boundingBox.b && inEntity.boundingBox.b < this.getEntityHandle().boundingBox.e)
		{
			if(!this.getEntityHandle().a(inEntity.getClass()))
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
				else if(inEntity instanceof EntityHuman && !inAttackInvulnurablePlayer && ((EntityHuman)inEntity).abilities.isInvulnerable)
					return false;
					
				if(!this.getEntityHandle().e(MathHelper.floor(inEntity.locX), MathHelper.floor(inEntity.locY), MathHelper.floor(inEntity.locZ)))
					return false;
				else if(this.m_shouldCheckSight && !this.getEntityHandle().aA().canSee(inEntity))
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

	                if (this instanceof DesireDefendVillage) {
	                    reason = EntityTargetEvent.TargetReason.DEFEND_VILLAGE;
	                } else if (this instanceof DesireAttackTarget) {
	                    reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY;
	                } else if (this instanceof DesireAttackNearest) {
	                    if (inEntity instanceof EntityHuman) {
	                        reason = EntityTargetEvent.TargetReason.CLOSEST_PLAYER;
	                    }
	                } else if (this instanceof DesireProtectOwner) {
	                    reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER;
	                } else if (this instanceof DesireHelpAttacking) {
	                    reason = EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET;
	                }

	                org.bukkit.event.entity.EntityTargetLivingEntityEvent event = org.bukkit.craftbukkit.v1_4_6.event.CraftEventFactory.callEntityTargetLivingEvent(this.getEntityHandle(), inEntity, reason);
	                if (event.isCancelled() || event.getTarget() == null) {
	                    if (this.getEntityHandle() instanceof EntityCreature) {
	                        ((EntityCreature)this.getEntityHandle()).target = null;
	                    }
	                    return false;
	                } else if (inEntity.getBukkitEntity() != event.getTarget()) {
	                    this.getEntityHandle().b((EntityLiving)((CraftEntity) event.getTarget()).getHandle());
	                }
	                if (this.getEntityHandle() instanceof EntityCreature) {
	                    ((EntityCreature) this.getEntityHandle()).target = ((org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity) event.getTarget()).getHandle();
	                }
					
					
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean useAttack(EntityLiving inEntity)
	{
		this.m_lastAttackTick = 10 + this.getEntityHandle().aB().nextInt(5);
		PathEntity path = this.getEntityHandle().getNavigation().a(inEntity);
		
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
}
