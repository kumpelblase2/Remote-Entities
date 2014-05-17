package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteProjectileType;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will attack its target with a ranged attack when it is possible.
 */
public class DesireRangedAttack extends DesireBase
{
	protected EntityLiving m_target;
	@SerializeAs(pos = 1)
	protected RemoteProjectileType m_projectileType;
	protected int m_inRangeTick;
	protected int m_shootTicks;
	@SerializeAs(pos = 2)
	protected int m_shootMinDelay;
	@SerializeAs(pos = 3)
	protected int m_shootMaxDelay;
	@SerializeAs(pos = 4)
	protected float m_minDistance;
	protected float m_minDistanceSquared;

	@Deprecated
	public DesireRangedAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType)
	{
		this(inEntity, inProjectileType, 60);
	}

	@Deprecated
	public DesireRangedAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType, int inDelay)
	{
		this(inEntity, inProjectileType, inDelay, 8);
	}

	@Deprecated
	public DesireRangedAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType, int inDelay, float inMinDistance)
	{
		this(inEntity, inProjectileType, inDelay, inDelay, inMinDistance);
	}

	@Deprecated
	public DesireRangedAttack(RemoteEntity inEntity, RemoteProjectileType inProjectileType, int inMinDelay, int inMaxDelay, float inMinDistance)
	{
		super(inEntity);
		this.m_projectileType = inProjectileType;
		this.m_shootMinDelay = inMinDelay;
		this.m_shootMaxDelay = inMaxDelay;
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSquared = inMinDistance * inMinDistance;
		this.m_type = DesireType.FULL_CONCENTRATION;
		this.m_shootTicks = -1;
	}

	public DesireRangedAttack(RemoteProjectileType inProjectileType)
	{
		this(inProjectileType, 60);
	}

	public DesireRangedAttack(RemoteProjectileType inProjectileType, int inDelay)
	{
		this(inProjectileType, inDelay, 8);
	}

	public DesireRangedAttack(RemoteProjectileType inProjectileType, int inDelay, float inMinDistance)
	{
		this(inProjectileType, inDelay, inDelay, inMinDistance);
	}

	public DesireRangedAttack(RemoteProjectileType inProjectileType, int inMinDelay, int inMaxDelay, float inMinDistance)
	{
		super();
		this.m_projectileType = inProjectileType;
		this.m_shootMinDelay = inMinDelay;
		this.m_shootMaxDelay = inMaxDelay;
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSquared = inMinDistance * inMinDistance;
		this.m_type = DesireType.FULL_CONCENTRATION;
		this.m_shootTicks = -1;
	}

	@Override
	public void stopExecuting()
	{
		EntityTargetEvent.TargetReason reason = this.m_target.isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
		CraftEventFactory.callEntityTargetEvent(this.getEntityHandle(), null, reason);
		this.m_target = null;
		this.m_inRangeTick = 0;
		this.m_shootTicks = -1;
	}

	@Override
	public boolean update()
	{
		double dist = this.getEntityHandle().e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ);
		boolean canSee = NMSUtil.getEntitySenses(this.getEntityHandle()).canSee(this.m_target);

		if(canSee)
			this.m_inRangeTick++;
		else
			this.m_inRangeTick = 0;

		if(dist <= this.m_minDistanceSquared && this.m_inRangeTick >= 20)
			this.getNavigation().h();
		else
			this.getRemoteEntity().move((LivingEntity)this.m_target.getBukkitEntity());

		NMSUtil.getControllerLook(this.getEntityHandle()).a(this.m_target, 30, 30);
		float strength;
		if(--this.m_shootTicks == 0)
		{
			if(dist <= this.m_minDistanceSquared && canSee)
			{
				strength = MathHelper.sqrt(dist) / this.m_minDistance;
				float strength2 = strength;

				if(strength < 0.1F)
					strength2 = 0.1F;

				if(strength2 > 1F)
					strength2 = 1F;

				this.shoot(strength2);
				this.m_shootTicks = MathHelper.d(strength * (this.m_shootMaxDelay - this.m_shootMinDelay) + this.m_shootMinDelay);
			}
		}
		else if(this.m_shootTicks < 0)
		{
			strength = MathHelper.sqrt(dist) / this.m_minDistance;
			this.m_shootTicks = MathHelper.d(strength * (this.m_shootMaxDelay - this.m_shootMinDelay) + this.m_shootMinDelay);
		}

		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		EntityLiving target = NMSUtil.getGoalTarget(this.getEntityHandle());

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
		return this.shouldExecute() || !this.getNavigation().g();
	}

	protected void shoot(float inStrength)
	{
		EntityLiving entity = this.getEntityHandle();
		if(this.m_projectileType == RemoteProjectileType.ARROW)
		{
			EntityArrow arrow = new EntityArrow(this.getEntityHandle().world, this.getEntityHandle(), this.m_target, 1.6F, 12);
			entity.world.makeSound(entity, "random.bow", 1, 1F / (entity.aH().nextFloat() * 0.4F + 0.8F));
			entity.world.addEntity(arrow);
		}
		else if(this.m_projectileType == RemoteProjectileType.SNOWBALL)
		{
			EntitySnowball snowball = new EntitySnowball(entity.world, entity);
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.locY + this.m_target.getHeadHeight() - 1.100000023841858D - snowball.locY;
			double zDiff = this.m_target.locZ - entity.locZ;
			float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff) * 0.2F;

			snowball.shoot(xDiff, yDiff + dist, zDiff, 1.6F, 12);
			entity.world.makeSound(entity, "random.bow", 1, 1F / (entity.aH().nextFloat() * 0.4F + 0.8F));
		}
		else if(this.m_projectileType == RemoteProjectileType.SMALL_FIREBALL)
		{
			entity.world.a(null, 1009, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.boundingBox.b + (this.m_target.length / 2) - (entity.locY + (entity.length / 2));
			double zDiff = this.m_target.locZ - entity.locZ;
			float dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff) * 0.2F;
			EntitySmallFireball fireball = new EntitySmallFireball(entity.world, entity, xDiff + entity.aH().nextGaussian() * dist, yDiff, zDiff + entity.aH().nextGaussian() * dist);
			fireball.locY = entity.locY + (entity.length / 2) + 0.5D;
			entity.world.addEntity(fireball);
		}
		else if(this.m_projectileType == RemoteProjectileType.FIREBALL)
		{
			double xDiff = this.m_target.locX - entity.locX;
			double yDiff = this.m_target.boundingBox.b + (this.m_target.length / 2) - (entity.locY + (entity.length / 2));
			double zDiff = this.m_target.locZ - entity.locZ;
			double d = 4;
			Vec3D vec = entity.j(1F).a();
			entity.world.a(null, 1008, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
			EntityFireball fireball = new EntityLargeFireball(entity.world, entity, xDiff, yDiff, zDiff);
			fireball.locX = entity.locX + vec.a * d;
			fireball.locY = entity.locY + (entity.length / 2) + 0.5D;
			fireball.locZ = entity.locZ + vec.c * d;
			entity.world.addEntity(fireball);
		}
		else if(this.m_projectileType == RemoteProjectileType.POTION)
		{
			EntityPotion potion = new EntityPotion(entity.world, this.getEntityHandle(), 32732);
			potion.pitch -= 20;
			double d0 = this.m_target.locX + this.m_target.motX - entity.locX;
			double d1 = this.m_target.locY + (double)this.m_target.getHeadHeight() - 1.100000023841858D - entity.locY;
			double d2 = this.m_target.locZ + this.m_target.motZ - entity.locZ;
			float f = MathHelper.sqrt(d0 * d0 + d2 * d2);

			if(f >= 8.0F && !this.m_target.hasEffect(MobEffectList.SLOWER_MOVEMENT))
				potion.setPotionValue(32698);
			else if(this.m_target.getHealth() >= 8 && !this.m_target.hasEffect(MobEffectList.POISON))
				potion.setPotionValue(32660);
			else if(f <= 3.0F && !this.m_target.hasEffect(MobEffectList.WEAKNESS) && entity.aH().nextFloat() < 0.25F)
				potion.setPotionValue(32696);

			potion.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
			entity.world.addEntity(potion);
		}
		else if(entity instanceof IRangedEntity)
			((IRangedEntity)entity).a(this.m_target, inStrength);
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
