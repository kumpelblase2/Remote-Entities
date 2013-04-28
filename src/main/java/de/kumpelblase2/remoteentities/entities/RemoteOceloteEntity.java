package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.*;
import de.kumpelblase2.remoteentities.api.features.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.*;
import net.minecraft.server.v1_5_R2.*;
import net.minecraft.server.v1_5_R2.Item;
import net.minecraft.server.v1_5_R2.World;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import java.lang.reflect.*;

public class RemoteOceloteEntity extends EntityOcelot implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	
	public RemoteOceloteEntity(World world)
	{
		this(world, null);
	}
	
	public RemoteOceloteEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
		try
		{
			Field temptField = EntityOcelot.class.getDeclaredField("e");
			temptField.setAccessible(true);
			temptField.set(this, new DesireTemptTemp(this.getRemoteEntity()));
			Field sitField = EntityTameableAnimal.class.getDeclaredField("d");
			sitField.setAccessible(true);
			sitField.set(this, new DesireSitTemp(this.getRemoteEntity()));
		}
		catch(Exception e)
		{
		}
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
	public void b_(EntityHuman entity)
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
		super.b_(entity);
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
	
	public static DesireItem[] getDefaultMovementDesires(RemoteEntity inEntityFor)
	{
		try
		{
			return new DesireItem[] { 
					new DesireItem(new DesireSwim(inEntityFor), 1),
					new DesireItem(new DesireSit(inEntityFor), 2),
					new DesireItem(new DesireTempt(inEntityFor, Item.RAW_FISH.id, true), 3),
					new DesireItem(new DesireAvoidSpecific(inEntityFor, 16F, 0.4F, 0.23F, EntityHuman.class), 4),
					new DesireItem(new DesireFollowTamer(inEntityFor, 5, 10), 5),
					new DesireItem(new DesireSitOnBlock(inEntityFor), 6),
					new DesireItem(new DesireLeapAtTarget(inEntityFor, 0.4F), 7),
					new DesireItem(new DesireOcelotAttack(inEntityFor), 8),
					new DesireItem(new DesireBreed(inEntityFor), 9),
					new DesireItem(new DesireWanderAround(inEntityFor), 10),
					new DesireItem(new DesireLookAtNearest(inEntityFor, EntityHuman.class, 10F), 11)
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
					new DesireItem(new DesireNonTamedFindNearest(inEntityFor, EntityChicken.class, 14, false, true, 750), 1)
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new DesireItem[0];
		}
	}
}
