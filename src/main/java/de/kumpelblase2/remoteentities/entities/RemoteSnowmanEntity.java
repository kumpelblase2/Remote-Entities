package de.kumpelblase2.remoteentities.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.RemoteProjectileType;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityInteractEvent;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;

public class RemoteSnowmanEntity extends EntitySnowman implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected int m_maxHealth;
	public static int defaultMaxHealth = 4;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	
	public RemoteSnowmanEntity(World world)
	{
		this(world, null);
	}
	
	public RemoteSnowmanEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
		this.m_maxHealth = defaultMaxHealth;
	}
	
	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature(InventoryFeature.class))
			return null;
		
		return ((InventoryFeature)this.m_remoteEntity.getFeatures().getFeature(InventoryFeature.class)).getInventory();
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public void setupStandardGoals()
	{
		try
		{
			Mind mind = this.getRemoteEntity().getMind();
			mind.addMovementDesire(new DesireRangedAttack(this.getRemoteEntity(), RemoteProjectileType.SNOWBALL), 1);
			mind.addMovementDesire(new DesireWanderAround(this.getRemoteEntity()), 2);
			mind.addMovementDesire(new DesireLookAtNearest(this.getRemoteEntity(), EntityHuman.class, 6), 3);
			mind.addMovementDesire(new DesireLookRandomly(this.getRemoteEntity()), 4);
			mind.addActionDesire(new DesireAttackNearest(this.getRemoteEntity(), EntityMonster.class, 16, false, true, 0), 1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void g(double x, double y, double z)
	{		
		if(this.m_remoteEntity != null && this.m_remoteEntity.isPushable() && !this.m_remoteEntity.isStationary())
			super.g(x, y, z);
	}
	
	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;
		
		super.move(d0, d1, d2);
	}

	@Override
	public void setMaxHealth(int inHealth)
	{
		this.m_maxHealth = inHealth;
	}
	
	@Override
	public int getMaxHealth()
	{
		if(this.m_maxHealth == 0)
			return defaultMaxHealth;
		return this.m_maxHealth;
	}
	
	@Override
	public void j_()
	{
		super.j_();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
	}
	
	@Override
	public void c_(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null || this.getRemoteEntity().getMind() == null)
			return;
		
		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Touch"))
		{
			if (this.m_lastBouncedId != entity.id || System.currentTimeMillis() - this.m_lastBouncedTime > 1000)
			{
				if(entity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
				{
					RemoteEntityTouchEvent event = new RemoteEntityTouchEvent(this.m_remoteEntity, entity.getBukkitEntity());
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())
						return;
					
					((TouchBehavior)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch((Player)entity.getBukkitEntity());
					this.m_lastBouncedTime = System.currentTimeMillis();
					this.m_lastBouncedId = entity.id;
				}
			}
		}
		super.c_(entity);
	}
	
	@Override
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null || this.getRemoteEntity().getMind() == null)
			return super.a(entity);
		
		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Interact"))
		{
			RemoteEntityInteractEvent event = new RemoteEntityInteractEvent(this.m_remoteEntity, (Player)entity.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return super.a(entity);
			
			((InteractBehavior)this.getRemoteEntity().getMind().getBehaviour("Interact")).onInteract((Player)entity.getBukkitEntity());
		}
		
		return super.a(entity);
	}	
	
	@Override
	public void die(DamageSource damagesource)
	{
		if(this.getRemoteEntity() != null && this.getRemoteEntity().getMind() != null)
		{
			this.getRemoteEntity().getMind().clearMovementDesires();
			this.getRemoteEntity().getMind().clearActionDesires();
		}
		super.die(damagesource);
	}
}
