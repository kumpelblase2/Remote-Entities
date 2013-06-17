package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_5_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.*;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteSkeletonEntity extends EntitySkeleton implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteSkeletonEntity(World world)
	{
		super(world);
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
	}

	public RemoteSkeletonEntity(World world, RemoteEntity inEntity)
	{
		this(world);
		this.m_remoteEntity = inEntity;
		this.setEquipment(0, new ItemStack(Item.BOW));
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
		Mind mind = this.getRemoteEntity().getMind();
		mind.addMovementDesires(getDefaultMovementDesires(this.getRemoteEntity()));
		mind.addTargetingDesires(getDefaultTargetingDesires(this.getRemoteEntity()));
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
	public void collide(Entity inEntity)
	{
		if(this.getRemoteEntity() == null || this.getRemoteEntity().getMind() == null)
		{
			super.collide(inEntity);
			return;
		}

		if (this.m_lastBouncedId != inEntity.id || System.currentTimeMillis() - this.m_lastBouncedTime > 1000)
		{
			RemoteEntityTouchEvent event = new RemoteEntityTouchEvent(this.m_remoteEntity, inEntity.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return;

			if(inEntity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Touch"))
			{
				if(inEntity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
					((TouchBehavior)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch((Player)inEntity.getBukkitEntity());
			}
		}

		this.m_lastBouncedTime = System.currentTimeMillis();
		this.m_lastBouncedId = inEntity.id;
		super.collide(inEntity);
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
	public void l_()
	{
		super.l_();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
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

	public static DesireItem[] getDefaultMovementDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[] {
				new DesireItem(new DesireSwim(inEntityFor), 1),
				new DesireItem(new DesireRestrictSun(inEntityFor), 2),
				new DesireItem(new DesireAvoidSun(inEntityFor), 3),
				new DesireItem(new DesireRangedAttack(inEntityFor, RemoteProjectileType.ENTITY_DEFAULT, 60), 4),
				new DesireItem(new DesireWanderAround(inEntityFor), 5),
				new DesireItem(new DesireLookAtNearest(inEntityFor, EntityHuman.class, 8), 6),
				new DesireItem(new DesireLookRandomly(inEntityFor), 6)
		};
	}

	public static DesireItem[] getDefaultTargetingDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[] {
				new DesireItem(new DesireFindAttackingTarget(inEntityFor, 16, false, false), 1),
				new DesireItem(new DesireFindNearestTarget(inEntityFor, EntityHuman.class, 16, false, true, 0), 2)
		};
	}
}
