package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_6_R1.*;
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

public class RemoteCreeperEntity extends EntityCreeper implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteCreeperEntity(World world)
	{
		this(world, null);
	}

	public RemoteCreeperEntity(World world, RemoteEntity inRemoteEntity)
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
		Mind mind = this.getRemoteEntity().getMind();
		mind.addMovementDesires(getDefaultMovementDesires(this.getRemoteEntity()));
		mind.addTargetingDesires(getDefaultTargetingDesires(this.getRemoteEntity()));
	}

	@Override
	public boolean bb()
	{
		return true;
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

	public static DesireItem[] getDefaultMovementDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[] {
				new DesireItem(new DesireSwim(inEntityFor), 1),
				new DesireItem(new DesireSwell(inEntityFor), 2),
				new DesireItem(new DesireAvoidSpecific(inEntityFor, 6f, 0.25f, 0.3f, EntityOcelot.class), 3),
				new DesireItem(new DesireMoveAndMeleeAttack(inEntityFor, null, false), 4),
				new DesireItem(new DesireWanderAround(inEntityFor), 5),
				new DesireItem(new DesireLookAtNearest(inEntityFor, EntityHuman.class, 8), 6),
				new DesireItem(new DesireLookRandomly(inEntityFor), 6)
		};
	}

	public static DesireItem[] getDefaultTargetingDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[] {
				new DesireItem(new DesireFindNearestTarget(inEntityFor, EntityHuman.class, 16, true, 0), 1),
				new DesireItem(new DesireFindAttackingTarget(inEntityFor, 16, false, false), 2)
		};
	}
}