package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteVillagerEntity extends EntityVillager implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteVillagerEntity(World world)
	{
		this(world, null);
	}

	public RemoteVillagerEntity(World inWorld, RemoteEntity inRemoteEntity)
	{
		super(inWorld);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
	}

	@Override
	public EntityAgeable createChild(EntityAgeable inEntityAgeable)
	{
		return this.b(inEntityAgeable);
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
		this.getRemoteEntity().getMind().addMovementDesires(getDefaultMovementDesires(this.getRemoteEntity()));
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
		try
		{
			return new DesireItem[] {
					new DesireItem(new DesireSwim(inEntityFor), 0),
					new DesireItem(new DesireAvoidSpecific(inEntityFor, 8f, 0.35f, 0.3f, EntityZombie.class), 1),
					new DesireItem(new DesireTradeWithPlayer(inEntityFor), 1),
					new DesireItem(new DesireLookAtTrader(inEntityFor, 8), 1),
					new DesireItem(new DesireMoveIndoors(inEntityFor), 2),
					new DesireItem(new DesireRestrictOpenDoor(inEntityFor), 3),
					new DesireItem(new DesireOpenDoor(inEntityFor, true, false), 4),
					new DesireItem(new DesireMoveTowardsRestriction(inEntityFor), 5),
					new DesireItem(new DesireMakeLove(inEntityFor), 6),
					new DesireItem(new DesireAcceptFlower(inEntityFor), 7),
					new DesireItem(new DesirePlay(inEntityFor), 8),
					new DesireItem(new DesireInteract(inEntityFor, EntityHuman.class, 3), 9),
					new DesireItem(new DesireInteract(inEntityFor, EntityVillager.class, 5), 9),
					new DesireItem(new DesireWanderAround(inEntityFor), 9),
					new DesireItem(new DesireLookAtNearest(inEntityFor, EntityInsentient.class, 8), 10)
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
		return new DesireItem[0];
	}
}