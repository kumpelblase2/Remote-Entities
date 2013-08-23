package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_6_R2.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.*;
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
		this.getRemoteEntity().getMind().addMovementDesires(getDefaultMovementDesires());
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

		return ((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()) && super.a(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	@Override
	protected String r()
	{
		return this.m_remoteEntity.getSound(EntitySound.RANDOM, (this.bW() ? "haggle" : "idle"));
	}

	@Override
	protected String aN()
	{
		return this.m_remoteEntity.getSound(EntitySound.HURT);
	}

	@Override
	protected String aO()
	{
		return this.m_remoteEntity.getSound(EntitySound.DEATH);
	}

	@Override
	public void a_(ItemStack itemstack) {
		//Taken from EntityVillager.java#264 - 273
		//modified to work with custom sounds
		if (!this.world.isStatic && this.a_ > -this.o() + 20) {
			this.a_ = -this.o();
			if (itemstack != null) {
				this.makeSound(this.m_remoteEntity.getSound(EntitySound.YES), this.aZ(), this.ba());
			} else {
				this.makeSound(this.m_remoteEntity.getSound(EntitySound.NO), this.aZ(), this.ba());
			}
		}
	}

	public static DesireItem[] getDefaultMovementDesires()
	{
		try
		{
			return new DesireItem[] {
					new DesireItem(new DesireSwim(), 0),
					new DesireItem(new DesireAvoidSpecific(8f, 0.6D, 0.6D, EntityZombie.class), 1),
					new DesireItem(new DesireTradeWithPlayer(), 1),
					new DesireItem(new DesireLookAtTrader(8), 1),
					new DesireItem(new DesireMoveIndoors(), 2),
					new DesireItem(new DesireRestrictOpenDoor(), 3),
					new DesireItem(new DesireOpenDoor(true, false), 4),
					new DesireItem(new DesireMoveTowardsRestriction(), 5),
					new DesireItem(new DesireMakeLove(), 6),
					new DesireItem(new DesireAcceptFlower(), 7),
					new DesireItem(new DesirePlay(0.32D), 8),
					new DesireItem(new DesireInteract(EntityHuman.class, 3, 1f), 9),
					new DesireItem(new DesireInteract(EntityVillager.class, 5, 0.2f), 9),
					new DesireItem(new DesireWanderAround(), 9),
					new DesireItem(new DesireLookAtNearest(EntityInsentient.class, 8), 10)
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new DesireItem[0];
		}
	}

	public static DesireItem[] getDefaultTargetingDesires()
	{
		return new DesireItem[0];
	}
}