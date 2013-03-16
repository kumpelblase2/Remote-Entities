package de.kumpelblase2.remoteentities.entities;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.v1_5_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityInteractEvent;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

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
		
		return ((InventoryFeature)this.m_remoteEntity.getFeatures().getFeature(InventoryFeature.class)).getInventory();
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
		try
		{
			Mind mind = this.getRemoteEntity().getMind();
			mind.addMovementDesire(new DesireSwim(this.getRemoteEntity()), 1);
			mind.addMovementDesire(new DesireSit(this.getRemoteEntity()), 2);
			mind.addMovementDesire(new DesireTempt(this.getRemoteEntity(), Item.RAW_FISH.id, true), 3);
			mind.addMovementDesire(new DesireAvoidSpecific(this.getRemoteEntity(), 16F, 0.4F, 0.23F, EntityHuman.class), 4);
			mind.addMovementDesire(new DesireFollowTamer(this.getRemoteEntity(), 5, 10), 5);
			mind.addMovementDesire(new DesireSitOnBlock(this.getRemoteEntity()), 6);
			mind.addMovementDesire(new DesireLeapAtTarget(this.getRemoteEntity(), 0.4F), 7);
			mind.addMovementDesire(new DesireOcelotAttack(this.getRemoteEntity()), 8);
			mind.addMovementDesire(new DesireBreed(this.getRemoteEntity()), 9);
			mind.addMovementDesire(new DesireWanderAround(this.getRemoteEntity()), 10);
			mind.addMovementDesire(new DesireLookAtNearest(this.getRemoteEntity(), EntityHuman.class, 10F), 11);
			mind.addTargetingDesire(new DesireNonTamedFindNearest(this.getRemoteEntity(), EntityChicken.class, 14, false, true, 750), 1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
}
