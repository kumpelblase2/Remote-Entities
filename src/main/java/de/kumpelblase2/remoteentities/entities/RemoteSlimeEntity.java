package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.RideBehavior;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteSlimeEntity extends EntitySlime implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_jumpDelay = 0;
	protected Entity m_target;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteSlimeEntity(World world)
	{
		this(world, null);
	}

	public RemoteSlimeEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
		this.m_jumpDelay = this.random.nextInt(20) + 10;
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

	public void setTarget(Entity inEntity)
	{
		this.m_target = inEntity;
	}

	public Entity getTarget()
	{
		return this.m_target;
	}

	@Override
	public void h()
	{
		super.h();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
	}

	@Override
	protected void bq()
	{
		this.w();
		if(this.m_target != null)
			this.a(this.m_target, 10.0F, 20.0F);

		// --- Taken from EntitySlime.java#103 - #121
		if (this.onGround && this.m_jumpDelay-- <= 0) {
			this.m_jumpDelay = this.bP();
			if (this.m_target != null) {
				this.m_jumpDelay /= 3;
			}

			this.bd = true;
			if (this.bW()) {
				this.makeSound(this.bT(), this.bf(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}

			this.be = 1.0F - this.random.nextFloat() * 2.0F;
			this.bf = (float)this.getSize();
		} else {
			this.bd = false;
			if (this.onGround) {
				this.be = this.bf = 0.0F;
			}
		}
		// ---
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
		if(this.m_remoteEntity.getMind().hasBehaviour("Ride"))
			((RideBehavior)this.m_remoteEntity.getMind().getBehaviour("Ride")).ride(motion);

		this.motY = (double)motion[2];
		super.e(motion[0], motion[1]);
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
	public boolean c(EntityLiving entity)
	{
		if(this.getRemoteEntity() == null)
			return super.c(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.c(entity);

		return ((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity(), false) && super.c(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	@Override
	public boolean bk()
	{
		return true;
	}

	@Override
	protected String aT()
	{
		return this.m_remoteEntity.getSound(EntitySound.HURT, (this.getSize() > 1 ? "big" : "small"));
	}

	@Override
	protected String aU()
	{
		return this.m_remoteEntity.getSound(EntitySound.DEATH, (this.getSize() > 1 ? "big" : "small"));
	}

	public static DesireItem[] getDefaultMovementDesires()
	{
		return new DesireItem[0];
	}

	public static DesireItem[] getDefaultTargetingDesires()
	{
		return new DesireItem[0];
	}
}