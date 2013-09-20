package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_6_R3.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteSpiderEntity extends EntitySpider implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteSpiderEntity(World world)
	{
		super(world);
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
	}

	public RemoteSpiderEntity(World world, RemoteEntity inEntity)
	{
		this(world);
		this.m_remoteEntity = inEntity;
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
		mind.addMovementDesires(getDefaultMovementDesires());
		mind.addTargetingDesires(getDefaultTargetingDesires());
	}

	@Override
	public boolean bf()
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
	public void e(float inXMotion, float inZMotion)
	{
		float[] motion = new float[] { inXMotion, inZMotion, 0 };
		if(this.m_remoteEntity.getMind().hasBehaviour("Ride"))
			((RideBehavior)this.m_remoteEntity.getMind().getBehaviour("Ride")).ride(motion);

		super.e(motion[0], motion[1]);
		this.motY = motion[2];
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
	public Entity findTarget()
	{
		return this.getGoalTarget();
	}

	@Override
	protected String r()
	{
		return this.m_remoteEntity.getSound(EntitySound.RANDOM);
	}

	@Override
	protected String aO()
	{
		return this.m_remoteEntity.getSound(EntitySound.HURT);
	}

	@Override
	protected String aP()
	{
		return this.m_remoteEntity.getSound(EntitySound.DEATH);
	}

	@Override
	protected void a(int i, int j, int k, int l)
	{
		this.makeSound(this.m_remoteEntity.getSound(EntitySound.STEP), 0.15F, 1.0F);
	}

	public static DesireItem[] getDefaultMovementDesires()
	{
		return new DesireItem[] {
				new DesireItem(new DesireSwim(), 1),
				new DesireItem(new DesireLeapAtTarget(1), 2),
				new DesireItem(new DesireMoveTowardsRestriction(), 3),
				new DesireItem(new DesireWanderAround(), 4),
				new DesireItem(new DesireLookAtNearest(EntityHuman.class, 8), 5),
				new DesireItem(new DesireLookRandomly(), 6)
		};
	}

	public static DesireItem[] getDefaultTargetingDesires()
	{
		return new DesireItem[] {
				new DesireItem(new DesireFindAttackingTarget(16, false, false), 1),
				new DesireItem(new DesireFindNearestTargetAtNight(EntityHuman.class, 16, false, true, 0), 2)
		};
	}
}