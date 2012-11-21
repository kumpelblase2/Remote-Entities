package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLargeFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPotion;
import net.minecraft.server.EntitySmallFireball;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteProjectileType;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireRangedAttack extends DesireBase
{
	protected EntityLiving m_target;
	protected RemoteProjectileType m_projeProjectileType;
	protected int m_inRangeTick;
	protected int m_shootTicks;
	protected int m_shootDelay;
	
	public DesireRangedAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType)
	{
		this(inEntity, inProjectileType, 60);
	}
	
	public DesireRangedAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType, int inDelay)
	{
		super(inEntity);
		this.m_projeProjectileType = inProjectileType;
		this.m_shootDelay = inDelay;
		this.m_type = 3;
	}

	@Override
	public void stopExecuting()
	{
		EntityTargetEvent.TargetReason reason = this.m_target.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
		CraftEventFactory.callEntityTargetEvent((Entity)this.getEntityHandle(), null, reason);
		this.m_target = null;
		this.m_inRangeTick = 0;
	}

	@Override
	public boolean update()
	{
		double maxDist = 100;
		double dist = this.getEntityHandle().e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ);
		boolean canSee = this.getEntityHandle().aA().canSee(this.m_target);
		
		if(canSee)
			this.m_inRangeTick++;
		else
			this.m_inRangeTick = 0;
		
		if(dist <= maxDist && this.m_inRangeTick >= 20)
			this.getEntityHandle().getNavigation().g();
		else
			this.getRemoteEntity().move((LivingEntity)this.m_target.getBukkitEntity());
		
		this.getEntityHandle().getControllerLook().a(this.m_target, 30, 30);
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
		if(this.getEntityHandle() == null)
			return false;
		
		EntityLiving target = this.getEntityHandle().aG();
		
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
		return this.shouldExecute() || !this.getEntityHandle().getNavigation().f();
	}
	
	protected void shoot()
	{
		EntityLiving entity = this.getEntityHandle();
		if(this.m_projeProjectileType == RemoteProjectileType.ARROW)
		{
			EntityArrow arrow = new EntityArrow(this.getEntityHandle().world, this.getEntityHandle(), this.m_target, 1.6F, 12);
			entity.world.makeSound(entity, "random.bow", 1, 1F / (entity.aB().nextFloat() * 0.4F + 0.8F));
			entity.world.addEntity(arrow);
		}
		else if(this.m_projeProjectileType == RemoteProjectileType.SNOWBALL)
		{
			EntitySnowball snowball = new EntitySnowball(entity.world, entity);
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.locY + this.m_target.getHeadHeight() - 1.100000023841858D - snowball.locY;
			double zDiff = this.m_target.locZ - entity.locZ;
			float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff) * 0.2F;
			
			snowball.shoot(xDiff, yDiff + dist, zDiff, 1.6F, 12);
			entity.world.makeSound(entity, "random.bow", 1, 1F / (entity.aB().nextFloat() * 0.4F + 0.8F));
		}
		else if(this.m_projeProjectileType == RemoteProjectileType.SMALL_FIREBALL)
		{
			entity.world.a((EntityHuman)null, 1009, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.boundingBox.b + (this.m_target.length / 2) - (entity.locY + (entity.length / 2));
			double zDiff = this.m_target.locZ - entity.locZ;
			float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff) * 0.2F;
			EntitySmallFireball fireball = new EntitySmallFireball(entity.world, entity, xDiff + entity.aB().nextGaussian() * dist, yDiff, zDiff + entity.aB().nextGaussian() * dist);
			fireball.locY = entity.locY + (entity.length / 2) + 0.5D;
			entity.world.addEntity(fireball);
		}
		else if(this.m_projeProjectileType == RemoteProjectileType.FIREBALL)
		{
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.boundingBox.b + (this.m_target.length / 2) - (entity.locY + (entity.length / 2));
			double zDiff = this.m_target.locZ - entity.locZ;
			double d = 4;
			Vec3D vec = entity.i(1F);
			entity.world.a(null, 1008, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
			EntityFireball fireball = new EntityLargeFireball(entity.world, entity, xDiff, yDiff, zDiff);
			fireball.locX = entity.locX + vec.c * d;
			fireball.locY = entity.locY + (entity.length / 2) + 0.5D;
			fireball.locZ = entity.locZ + vec.e * d;
			entity.world.addEntity(fireball);
		}
		else
		{
			EntityPotion potion = new EntityPotion(entity.world, this.getEntityHandle(), 32732);
			potion.pitch -= 20;
			double d0 = this.m_target.locX + this.m_target.motX - entity.locX;
            double d1 = this.m_target.locY + (double) this.m_target.getHeadHeight() - 1.100000023841858D - entity.locY;
            double d2 = this.m_target.locZ + this.m_target.motZ - entity.locZ;
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);

            if (f >= 8.0F && !this.m_target.hasEffect(MobEffectList.SLOWER_MOVEMENT)) {
                potion.setPotionValue(32698);
            } else if (this.m_target.getHealth() >= 8 && !this.m_target.hasEffect(MobEffectList.POISON)) {
                potion.setPotionValue(32660);
            } else if (f <= 3.0F && !this.m_target.hasEffect(MobEffectList.WEAKNESS) && entity.aB().nextFloat() < 0.25F) {
                potion.setPotionValue(32696);
            }

            potion.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
            entity.world.addEntity(potion);
		}
	}
}
