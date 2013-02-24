package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.v1_4_R1.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.MathHelper;
import net.minecraft.server.v1_4_R1.PathEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSClassMap;

public class DesireAttackOnCollide extends DesireBase
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
	
	public DesireAttackOnCollide(RemoteEntity inEntity, Class<?> inToAttack, boolean inIgnoreSight)
	{
		this(inEntity, inToAttack, inIgnoreSight, inEntity.getSpeed());
	}
	
	@SuppressWarnings("unchecked")
	public DesireAttackOnCollide(RemoteEntity inEntity, Class<?> inToAttack, boolean inIgnoreSight, float inSpeed)
	{
		super(inEntity);
		this.m_speed = inSpeed;
		this.m_ignoreSight = inIgnoreSight;
		if(inToAttack.isAssignableFrom(Entity.class))
			this.m_toAttack = (Class<? extends Entity>)inToAttack;
		else
			this.m_toAttack = (Class<? extends Entity>)NMSClassMap.getNMSClass(inToAttack);
		
		this.m_attackTick = 0;
		this.m_type = 3;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		EntityLiving entityTarget = this.getEntityHandle().getGoalTarget();
		
		if(entityTarget == null)
			return false;
		else if(this.m_toAttack != null && !this.m_toAttack.isAssignableFrom(entityTarget.getClass()))
			return false;
		else
		{
			this.m_target = entityTarget;
			this.m_path = this.getEntityHandle().getNavigation().a(this.m_target);
			return this.m_path != null;
		}
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving entityTarget = this.getEntityHandle().getGoalTarget();
		EntityLiving entity = this.getEntityHandle();
		return entityTarget == null ? false : (!this.m_target.isAlive() ? false : (!this.m_ignoreSight ? !entity.getNavigation().f() : entity.e(MathHelper.floor(this.m_target.locX), MathHelper.floor(this.m_target.locY), MathHelper.floor(this.m_target.locZ))));
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
		this.getEntityHandle().getNavigation().g();
	}
	
	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		entity.getControllerLook().a(this.m_target, 30, 30);
		if((this.m_ignoreSight || entity.aA().canSee(this.m_target)) && --this.m_moveTick <= 0)
		{
			this.m_moveTick = 4 + entity.aB().nextInt(7);
			this.getRemoteEntity().move((LivingEntity)entity.getBukkitEntity(), this.m_speed);
		}
		
		this.m_attackTick = Math.max(this.m_attackTick - 1, 0);
		double minDist = entity.width * 2 * entity.width * 2;
		if(this.m_attackTick <= 0 && entity.e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ) <= minDist)
		{
			this.m_attackTick = 20;
			if(entity.bD() != null)
				entity.bH();
			
			entity.m(this.m_target);
		}
		return true;
	}
}