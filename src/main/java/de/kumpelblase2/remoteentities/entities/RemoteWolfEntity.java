package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteWolfEntity extends EntityWolf implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteWolfEntity(World world)
	{
		this(world, null);
	}

	public RemoteWolfEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
		this.bp = new DesireSitTemp(this.getRemoteEntity());
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
	public void l_()
	{
		super.l_();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
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
	public void collide(Entity inEntity)
	{
		if(this.getRemoteEntity() == null)
		{
			super.collide(inEntity);
			return;
		}

		if(((RemoteBaseEntity)this.m_remoteEntity).onCollide(inEntity.getBukkitEntity()))
			super.collide(inEntity);
	}

	@Override
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null)
			return super.a(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.a(entity);

		if(((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()))
			return super.a(entity);
		else
			return false;
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	@Override
	public Entity findTarget()
	{
		return this.getGoalTarget();
	}

	public static DesireItem[] getDefaultMovementDesires(RemoteEntity inEntityFor)
	{
		try
		{
			return new DesireItem[] {
					new DesireItem(new DesireSwim(inEntityFor), 1),
					new DesireItem(new DesireSit(inEntityFor), 2),
					new DesireItem(new DesireLeapAtTarget(inEntityFor, 0.4F), 3),
					new DesireItem(new DesireMoveAndMeleeAttack(inEntityFor, null, true), 4),
					new DesireItem(new DesireFollowTamer(inEntityFor, 2, 10), 5),
					new DesireItem(new DesireBreed(inEntityFor), 6),
					new DesireItem(new DesireWanderAround(inEntityFor), 7),
					new DesireItem(new DesireBegForItem(inEntityFor, 8f, Material.BONE), 8),
					new DesireItem(new DesireLookAtNearest(inEntityFor, EntityHuman.class, 8), 9),
					new DesireItem(new DesireLookRandomly(inEntityFor), 9)
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new DesireItem[0];
		}
	}

	public static DesireItem[] getDefaultTargetingDesires(RemoteEntity inEntityFor)
	{
		try
		{
			return new DesireItem[] {
					new DesireItem(new DesireProtectOwner(inEntityFor, 32, false), 1),
					new DesireItem(new DesireHelpAttacking(inEntityFor, 32, false), 2),
					new DesireItem(new DesireFindAttackingTarget(inEntityFor, 16, true, true), 3),
					new DesireItem(new DesireNonTamedFindNearest(inEntityFor, EntitySheep.class, 14, false, true, 750), 4)
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new DesireItem[0];
		}
	}
}