package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_5_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.events.*;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteEnderDragonEntity extends EntityEnderDragon implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

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
	public void g(double x, double y, double z)
	{
		RemoteEntityPushEvent event = new RemoteEntityPushEvent(this.getRemoteEntity(), new Vector(x, y, z));
		event.setCancelled(this.m_remoteEntity == null || !this.m_remoteEntity.isPushable() || this.m_remoteEntity.isStationary());
		Bukkit.getPluginManager().callEvent(event);

		if(!event.isCancelled())
		{
			Vector vel = event.getVelocity();
			super.g(vel.getX(), vel.getY(), vel.getZ());
		}
	}

	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;

		super.move(d0, d1, d2);
	}

	@Override
	public void l_()
	{
		super.l_();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
	}

	@Override
	public void c()
	{
		if(this.getRemoteEntity().isStationary())
		{
			// --- Taken from EntityEnderDragon.java#77 - #81
			if(this.health <= 0)
			{
				float f = (this.random.nextFloat() - 0.5F) * 8.0F;
				float d05 = (this.random.nextFloat() - 0.5F) * 4.0F;
	            float f1 = (this.random.nextFloat() - 0.5F) * 8.0F;
	            this.world.addParticle("largeexplode", this.locX + (double) f, this.locY + 2.0D + (double) d05, this.locZ + (double) f1, 0.0D, 0.0D, 0.0D);
			}
			// ---
			return;
		}

		super.c();
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
                d0 *= (double) (1.0F - this.aa);
                d1 *= (double) (1.0F - this.aa);
                this.g(-d0, 0.0D, -d1);
                entity.g(d0, 0.0D, d1);
            }
        }
		// ---

		if(this.getRemoteEntity() == null || this.getRemoteEntity().getMind() == null)
		{
			super.collide(entity);
			return;
		}

		if (this.m_lastBouncedId != entity.id || System.currentTimeMillis() - this.m_lastBouncedTime > 1000)
		{
			RemoteEntityTouchEvent event = new RemoteEntityTouchEvent(this.m_remoteEntity, entity.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return;

			if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Touch"))
			{
				if(entity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
					((TouchBehavior)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch((Player)entity.getBukkitEntity());
			}
		}

		this.m_lastBouncedTime = System.currentTimeMillis();
		this.m_lastBouncedId = entity.id;
		super.collide(entity);
	}

	@Override
	public boolean a_(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null || this.getRemoteEntity().getMind() == null)
			return super.a_(entity);

		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel())
		{
			RemoteEntityInteractEvent event = new RemoteEntityInteractEvent(this.m_remoteEntity, (Player)entity.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return super.a_(entity);

			if(this.getRemoteEntity().getMind().hasBehaviour("Interact"))
				((InteractBehavior)this.getRemoteEntity().getMind().getBehaviour("Interact")).onInteract((Player)entity.getBukkitEntity());
		}

		return super.a_(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		if(this.getRemoteEntity() != null && this.getRemoteEntity().getMind() != null)
		{
			this.getRemoteEntity().getMind().clearMovementDesires();
			this.getRemoteEntity().getMind().clearTargetingDesires();
		}
		super.die(damagesource);
	}

	@Override
	public boolean bh()
	{
		return true;
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
