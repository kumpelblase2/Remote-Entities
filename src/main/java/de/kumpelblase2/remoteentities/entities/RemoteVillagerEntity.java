package de.kumpelblase2.remoteentities.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.v1_4_R1.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityInteractEvent;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;

public class RemoteVillagerEntity extends EntityVillager implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	
	public RemoteVillagerEntity(World world)
	{
		this(world, null);
	}
	
	public RemoteVillagerEntity(World world, RemoteEntity inRemoteEntity)
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
			mind.addMovementDesire(new DesireSwim(this.getRemoteEntity()), 0);
			mind.addMovementDesire(new DesireAvoidSpecific(this.getRemoteEntity(), 8f, 0.35f, 0.3f, EntityZombie.class), 1);
			mind.addMovementDesire(new DesireTradeWithPlayer(this.getRemoteEntity()), 1);
			mind.addMovementDesire(new DesireLookAtTrader(this.getRemoteEntity(), 8), 1);
			mind.addMovementDesire(new DesireMoveIndoors(this.getRemoteEntity()), 2);
			mind.addMovementDesire(new DesireRestrictOpenDoor(this.getRemoteEntity()), 3);
			mind.addMovementDesire(new DesireOpenDoor(this.getRemoteEntity(), true, false), 4);
			mind.addMovementDesire(new DesireMoveTowardsRestriction(this.getRemoteEntity()), 5);
			mind.addMovementDesire(new DesireMakeLove(this.getRemoteEntity()), 6);
			mind.addMovementDesire(new DesireAcceptFlower(this.getRemoteEntity()), 7);
			mind.addMovementDesire(new DesirePlay(this.getRemoteEntity()), 8);
			mind.addMovementDesire(new DesireInteract(this.getRemoteEntity(), EntityHuman.class, 3), 9);
			mind.addMovementDesire(new DesireInteract(this.getRemoteEntity(), EntityVillager.class, 5), 9);
			mind.addMovementDesire(new DesireWanderAround(this.getRemoteEntity()), 9);
			mind.addMovementDesire(new DesireLookAtNearest(this.getRemoteEntity(), EntityLiving.class, 8), 10);
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
		
		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel())
		{
			RemoteEntityInteractEvent event = new RemoteEntityInteractEvent(this.m_remoteEntity, (Player)entity.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return super.a(entity);
			
			if(this.getRemoteEntity().getMind().hasBehaviour("Interact"))
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
