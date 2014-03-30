package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_7_R2.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.RideBehavior;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteEnderDragonEntity extends EntityEnderDragon implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	protected Location m_targetLocation;

	public RemoteEnderDragonEntity(World world)
	{
		this(world, null);
	}

	public RemoteEnderDragonEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
	}

	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature(InventoryFeature.class))
			return null;

		return this.m_remoteEntity.getFeatures().getFeature(InventoryFeature.class).getInventory();
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public void setupStandardGoals()
	{
	}

	@Override
	public void h()
	{
		super.h();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();

		if(this.m_targetLocation != null)
		{
			this.h = this.m_targetLocation.getX();
			this.i = this.m_targetLocation.getY();
			this.j = this.m_targetLocation.getZ();
		}
	}

	@Override
	public void e()
	{
		if(this.getRemoteEntity().isStationary())
		{
			// --- Taken from EntityEnderDragon.java#77 - #81
			if(this.getHealth() <= 0)
			{
				float f = (this.random.nextFloat() - 0.5F) * 8.0F;
				float d05 = (this.random.nextFloat() - 0.5F) * 4.0F;
	            float f1 = (this.random.nextFloat() - 0.5F) * 8.0F;
	            this.world.addParticle("largeexplode", this.locX + (double) f, this.locY + 2.0D + (double) d05, this.locZ + (double) f1, 0.0D, 0.0D, 0.0D);
			}
			// ---
			return;
		}
		else if(this.getRemoteEntity().getMind().hasBehavior(RideBehavior.class))
		{
			float[] mot = new float[] { 0, 0, 0 };
			this.getRemoteEntity().getMind().getBehavior(RideBehavior.class).ride(mot);
			super.e(mot[0], mot[1]);
			this.motY = mot[2];
			this.m_remoteEntity.setYaw((this.yaw < 0 ? this.yaw + 180 : this.yaw - 180));
			return;
		}

		if(((RemoteEnderDragon)this.m_remoteEntity).shouldNormallyFly())
			super.e();
		else
		{
			if(this.m_targetLocation != null)
			{
				if(this.getBukkitEntity().getLocation().distanceSquared(this.m_targetLocation) <= 4)
				{
					this.m_targetLocation = null;
					return;
				}

				this.h = this.m_targetLocation.getX();
				this.i = this.m_targetLocation.getY();
				this.j = this.m_targetLocation.getZ();

				super.e();
			}
		}
	}

	@Override
	public void collide(Entity entity)
	{
		// --- Taken from Entity.java#778 - #802
		if (entity.passenger != this && entity.vehicle != this) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;
            double d2 = MathHelper.a(d0, d1);

            if (d2 >= 0.009999999776482582D) {
                d2 = (double) MathHelper.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= (double) (1.0F - this.Z);
                d1 *= (double) (1.0F - this.Z);
                this.g(-d0, 0.0D, -d1);
                entity.g(d0, 0.0D, d1);
            }
        }
		// ---

		if(this.getRemoteEntity() == null)
		{
			super.collide(entity);
			return;
		}

		if(((RemoteBaseEntity)this.m_remoteEntity).onCollide(entity.getBukkitEntity()))
			super.collide(entity);
	}

	@Override
	public void g(double x, double y, double z)
	{
		if(this.m_remoteEntity == null)
		{
			super.g(x, y, z);
			return;
		}

		Vector vector = ((RemoteBaseEntity)this.m_remoteEntity).onPush(x, y, z);
		if(vector != null)
			super.g(vector.getX(), vector.getY(), vector.getZ());
	}

	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;

		super.move(d0, d1, d2);
	}

	@Override
	public void e(float inXMotion, float inZMotion)
	{
		float[] motion = new float[] { inXMotion, inZMotion, (float)this.motY };
		if(this.m_remoteEntity.getMind().hasBehavior(RideBehavior.class))
			this.m_remoteEntity.getMind().getBehavior(RideBehavior.class).ride(motion);

		this.motY = (double)motion[2];
		super.e(motion[0], motion[1]);
	}

	@Override
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null)
			return super.a(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.a(entity);

		return ((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()) && super.a(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	@Override
	public boolean bM()
	{
		return true;
	}

	@Override
	protected String t()
	{
		return this.m_remoteEntity.getSound(EntitySound.RANDOM);
	}

	@Override
	protected String aS()
	{
		return this.m_remoteEntity.getSound(EntitySound.HURT);
	}

	@Override
	protected String aT()
	{
		return this.m_remoteEntity.getSound(EntitySound.DEATH);
	}

	protected void setTargetLocation(Location inTarget)
	{
		this.m_targetLocation = inTarget;
	}

	public static DesireItem[] getDefaultMovementDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[0];
	}

	public static DesireItem[] getDefaultTargetingDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[0];
	}
}