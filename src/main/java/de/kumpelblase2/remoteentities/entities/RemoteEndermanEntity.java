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

public class RemoteEndermanEntity extends EntityEnderman implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteEndermanEntity(World world)
	{
		this(world, null);
	}

	public RemoteEndermanEntity(World world, RemoteEntity inRemoteEntity)
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
	protected String t()
	{
		return this.m_remoteEntity.getSound(EntitySound.RANDOM, (this.bX() ? "scream" : "idle"));
	}

	@Override
	protected String aT()
	{
		return this.m_remoteEntity.getSound(EntitySound.HURT);
	}

	@Override
	protected String aU()
	{
		return this.m_remoteEntity.getSound(EntitySound.DEATH);
	}

	@Override
	protected boolean k(double d0, double d1, double d2) {
		//Taken from EntityEnderman.java#206 - 263
		// modified to use custom sounds
		double d3 = this.locX;
		double d4 = this.locY;
		double d5 = this.locZ;

		this.locX = d0;
		this.locY = d1;
		this.locZ = d2;
		boolean flag = false;
		int i = MathHelper.floor(this.locX);
		int j = MathHelper.floor(this.locY);
		int k = MathHelper.floor(this.locZ);

		if (this.world.isLoaded(i, j, k)) {
			boolean flag1 = false;

			while (!flag1 && j > 0) {
				Block block = this.world.getType(i, j - 1, k);
				if (block.getMaterial() != Material.AIR && block.getMaterial().isSolid()) {
					flag1 = true;
				} else {
					--this.locY;
					--j;
				}
			}

			if (flag1) {
				this.setPosition(this.locX, this.locY, this.locZ);
				if (this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox)) {
					flag = true;
				}
			}
		}

		if (!flag) {
			this.setPosition(d3, d4, d5);
			return false;
		} else {
			short short1 = 128;

			for (int l = 0; l < short1; ++l) {
				double d6 = (double) l / ((double) short1 - 1.0D);
				float f = (this.random.nextFloat() - 0.5F) * 0.2F;
				float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
				float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (this.locX - d3) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;
				double d8 = d4 + (this.locY - d4) * d6 + this.random.nextDouble() * (double) this.length;
				double d9 = d5 + (this.locZ - d5) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;

				this.world.addParticle("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
			}

			this.world.makeSound(d3, d4, d5, this.m_remoteEntity.getSound(EntitySound.TELEPORT), 1.0F, 1.0F);
			this.makeSound(this.m_remoteEntity.getSound(EntitySound.TELEPORT), 1.0F, 1.0F);
			return true;
		}
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