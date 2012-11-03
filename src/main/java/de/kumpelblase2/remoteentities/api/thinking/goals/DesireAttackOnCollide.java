
package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireAttackOnCollide extends DesireBase
{
	protected Class<? extends EntityLiving> m_toAttack;
	protected EntityLiving m_target;
	protected int m_attackTick;
	protected int m_moveTick;
	protected PathEntity m_path;
	protected boolean m_ignoreSight;
	
	public DesireAttackOnCollide(RemoteEntity inEntity, Class<? extends EntityLiving> inToAttack, boolean inIgnoreSight)
	{
		super(inEntity);
		this.m_ignoreSight = inIgnoreSight;
		this.m_toAttack = inToAttack;
		this.m_attackTick = 0;
		this.m_type = 3;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entityTarget = this.getRemoteEntity().getHandle().az();
		
		if(entityTarget == null)
			return false;
		else if(this.m_toAttack != null && !this.m_toAttack.isAssignableFrom(entityTarget.getClass()))
			return false;
		else
		{
			this.m_target = entityTarget;
			this.m_path = this.getRemoteEntity().getHandle().getNavigation().a(this.m_target);
			return this.m_path != null;
		}
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving entityTarget = this.getRemoteEntity().getHandle().az();
		EntityLiving entity = this.getRemoteEntity().getHandle();
		return entityTarget == null ? false : (!this.m_target.isAlive() ? false : (!this.m_ignoreSight ? !entity.getNavigation().f() : entity.d(MathHelper.floor(this.m_target.locX), MathHelper.floor(this.m_target.locY), MathHelper.floor(this.m_target.locZ))));
	}
	
	@Override
	public void startExecuting()
	{		
		this.getRemoteEntity().getHandle().getNavigation().a(this.m_path, this.getRemoteEntity().getSpeed());
		this.m_moveTick = 0;
	}
	
	@Override
	public void stopExecuting()
	{
		EntityTargetEvent.TargetReason reason = this.m_target.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
        CraftEventFactory.callEntityTargetEvent(this.getRemoteEntity().getHandle(), null, reason);
		
		this.m_target = null;
		this.getRemoteEntity().getHandle().getNavigation().g();
	}
	
	@Override
	public boolean update()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		entity.getControllerLook().a(this.m_target, 30, 30);
		if((this.m_ignoreSight || entity.at().canSee(this.m_target)) && --this.m_moveTick <= 0)
		{
			this.m_moveTick = 4 + entity.au().nextInt(7);
			entity.getNavigation().a(this.m_target, this.getRemoteEntity().getSpeed());
		}
		
		this.m_attackTick = Math.max(this.m_attackTick - 1, 0);
		double minDist = entity.width * 2 * entity.width * 2;
		if(entity.e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ) <= minDist)
		{
			if(this.m_attackTick <= 0)
			{
				this.m_attackTick = 20;
				entity.k(this.m_target);
			}
		}
		return true;
	}
}
