package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.EntityLiving;
import org.bukkit.entity.Entity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

/**
 * Using this desire the entity will deal damage like ocelots do.
 */
public class DesireOcelotAttack extends DesireBase
{
	protected int m_attackTick;
	protected EntityLiving m_target;

	public DesireOcelotAttack(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		EntityLiving target = NMSUtil.getGoalTarget(this.getEntityHandle());
		if(target == null)
			return false;

		this.m_target = target;
		return false;
	}

	@Override
	public boolean canContinue()
	{
		return this.m_target.isAlive() && (this.getEntityHandle().e(this.m_target) <= 225 && (!NMSUtil.getNavigation(this.getEntityHandle()).g() || this.shouldExecute()));
	}

	@Override
	public void stopExecuting()
	{
		this.m_target = null;
		NMSUtil.getNavigation(this.getEntityHandle()).h();
	}

	@Override
	public boolean update()
	{
		EntityLiving entity = this.getEntityHandle();
		NMSUtil.getControllerLook(this.getEntityHandle()).a(this.m_target, 30, 30);
		double minDist = entity.width * entity.width * 4;
		double dist = entity.e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ);
		float speed = 0.23F;

		if(dist > minDist && dist < 16)
			speed = 0.4F;
		else if(dist < 255)
			speed = 0.18F;

		NMSUtil.getNavigation(entity).a(this.m_target, speed);
		this.m_attackTick = Math.min(this.m_attackTick - 1, 0);
		if(dist <= minDist)
		{
			if(this.m_attackTick <= 0)
			{
				this.m_attackTick = 20;
				this.attack(this.m_target.getBukkitEntity());
			}
		}
		return true;
	}

	public void attack(Entity inEntity)
	{
		this.getEntityHandle().m(this.m_target);
	}
}