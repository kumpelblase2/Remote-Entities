package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.PathEntity;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.api.thinking.OneTimeDesire;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;

public class DesireKillTarget extends DesireBase implements OneTimeDesire
{
	@SerializeAs(pos = 1)
	protected EntityLiving m_target;
	protected PathEntity m_path;
	protected int m_moveTick;
	protected int m_attackTick = 0;
	
	public DesireKillTarget(RemoteEntity inEntity, LivingEntity inTarget)
	{
		super(inEntity);
		this.m_target = ((CraftLivingEntity)inTarget).getHandle();
		this.m_type = DesireType.FULL_CONCENTRATION;
	}
	
	public DesireKillTarget(RemoteEntity inEntity, EntityLiving inTarget)
	{
		super(inEntity);
		this.m_target = inTarget;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(this.m_target == null)
			return false;
		
		if(!this.m_entity.getBukkitEntity().getLocation().getWorld().getName().equals(this.m_target.getBukkitEntity().getWorld().getName()))
			return false;
		
		this.m_path = this.getEntityHandle().getNavigation().a(this.m_target);
		return this.m_path != null;
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_target.isAlive();
	}
	
	@Override
	public void startExecuting()
	{
		this.movePath(this.m_path, this.m_entity.getSpeed());
		this.m_moveTick = 0;
	}

	@Override
	public void stopExecuting()
	{
		this.getEntityHandle().getNavigation().g();
	}
	
	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		entity.getControllerLook().a(this.m_target, 30, 30);
		if(--this.m_moveTick <= 0)
		{
			this.m_moveTick = 4 + entity.aE().nextInt(7);
			this.getRemoteEntity().move((LivingEntity)entity.getBukkitEntity(), this.getRemoteEntity().getSpeed());
		}
		
		this.m_attackTick = Math.max(this.m_attackTick - 1, 0);
		double minDist = entity.width * 2 * entity.width * 2;
		if(this.m_attackTick <= 0 && entity.e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ) <= minDist)
		{
			this.m_attackTick = 20;
			if(entity.bG() != null)
				this.getEntityHandle().bK();
			
			this.attack((LivingEntity)this.m_target.getBukkitEntity());
		}
		return true;
	}
	
	public void attack(LivingEntity inEntity)
	{
		this.getEntityHandle().m(((CraftLivingEntity)inEntity).getHandle());
	}

	@Override
	public boolean isFinished()
	{
		return !this.m_target.isAlive();
	}
}
