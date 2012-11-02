package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.entity.LivingEntity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySmallFireball;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteProjectileType;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireArrowAttack extends DesireBase
{
	protected EntityLiving m_target;
	protected RemoteProjectileType m_projeProjectileType;
	protected int m_inRangeTick;
	protected int m_shootTicks;
	protected int m_shootDelay;
	
	public DesireArrowAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType)
	{
		this(inEntity, inProjectileType, 60);
	}
	
	public DesireArrowAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType, int inDelay)
	{
		super(inEntity);
		this.m_projeProjectileType = inProjectileType;
		this.m_shootDelay = inDelay;
		this.m_type = 3;
	}

	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}

	@Override
	public boolean update()
	{
		double maxDist = 100;
		double dist = this.getRemoteEntity().getHandle().e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ);
		boolean canSee = this.getRemoteEntity().getHandle().at().canSee(this.m_target);
		
		if(canSee)
			this.m_inRangeTick++;
		else
			this.m_inRangeTick = 0;
		
		if(dist <= maxDist && this.m_inRangeTick >= 20)
			this.getRemoteEntity().getHandle().getNavigation().g();
		else
			this.getRemoteEntity().move((LivingEntity)this.m_target.getBukkitEntity());
		
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_target, 30, 30);
		this.m_shootTicks = Math.max(this.m_shootTicks - 1, 0);
		if(this.m_shootTicks <= 0)
		{
			if(dist <= maxDist && canSee)
			{
				this.shoot();
				this.m_shootTicks = this.m_shootDelay;
			}
		}
		
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving target = this.getRemoteEntity().getHandle().az();
		
		if(target == null)
			return false;
		else
		{
			this.m_target = target;
			return true;
		}
	}

	@Override
	public boolean canContinue()
	{
		return this.shouldExecute() || !this.getRemoteEntity().getHandle().getNavigation().f();
	}
	
	protected void shoot()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		if(this.m_projeProjectileType == RemoteProjectileType.ARROW)
		{
			EntityArrow arrow = new EntityArrow(this.getRemoteEntity().getHandle().world, this.getRemoteEntity().getHandle(), this.m_target, 1.6F, 12);
			entity.world.makeSound(entity, "random.bow", 1, 1F / (entity.au().nextFloat() * 0.4F + 0.8F));
			entity.world.addEntity(arrow);
		}
		else if(this.m_projeProjectileType == RemoteProjectileType.SNOWBALL)
		{
			EntitySnowball snowball = new EntitySnowball(entity.world, entity);
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.locY + this.m_target.getHeadHeight() - 1.100000023841858D - snowball.locY;
			double zDiff = this.m_target.locZ - entity.locZ;
			float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff) * 0.2F;
			
			snowball.c(xDiff, yDiff + dist, zDiff, 1.6F, 12);
			entity.world.makeSound(entity, "random.bow", 1, 1F / (entity.au().nextFloat() * 0.4F + 0.8F));
		}
		else if(this.m_projeProjectileType == RemoteProjectileType.SMALL_FIREBALL)
		{
			entity.world.a((EntityHuman)null, 1009, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.boundingBox.b + (this.m_target.length / 2) - (entity.locY + (entity.length / 2));
			double zDiff = this.m_target.locZ - entity.locZ;
			float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff) * 0.2F;
			EntitySmallFireball fireball = new EntitySmallFireball(entity.world, entity, xDiff + entity.au().nextGaussian() * dist, yDiff, zDiff + entity.au().nextGaussian() * dist);
			fireball.locY = entity.locY + (entity.length / 2) + 0.5D;
			entity.world.addEntity(fireball);
		}
		else
		{
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.boundingBox.b + (this.m_target.length / 2) - (entity.locY + (entity.length / 2));
			double zDiff = this.m_target.locZ - entity.locZ;
			double d = 4;
			Vec3D vec = entity.i(1F);
			entity.world.a(null, 1008, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
			EntityFireball fireball = new EntityFireball(entity.world, entity, xDiff, yDiff, zDiff);
			fireball.locX = entity.locX + vec.a * d;
			fireball.locY = entity.locY + (entity.length / 2) + 0.5D;
			fireball.locZ = entity.locZ + vec.c * d;
			entity.world.addEntity(fireball);
		}
	}
}
