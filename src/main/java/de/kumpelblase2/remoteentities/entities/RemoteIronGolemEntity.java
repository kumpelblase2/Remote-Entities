package de.kumpelblase2.remoteentities.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.World;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.InteractBehavior;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.api.thinking.PathfinderGoalSelectorHelper;
import de.kumpelblase2.remoteentities.api.thinking.TouchBehavior;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteIronGolemEntity extends EntityIronGolem implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected int m_maxHealth;
	public static int defaultMaxHealth = 100;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	
	static
	{
		ReflectionUtil.registerEntityType(RemoteIronGolemEntity.class, "VillagerGolem", 99);
	}
	
	public RemoteIronGolemEntity(World world)
	{
		this(world, null);
	}
	
	public RemoteIronGolemEntity(World world, RemoteEntity inRemoteEntity)
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
			mind.addMovementDesire(new DesireAttackOnCollide(this.getRemoteEntity(), null, true), 1);
			mind.addMovementDesire(new DesireMoveToTarget(this.getRemoteEntity(), 32), 2);
			mind.addMovementDesire(new DesireMoveThroughVillage(this.getRemoteEntity(), true), 3);
			mind.addMovementDesire(new DesireMoveTowardsRestriction(this.getRemoteEntity()), 4);
			mind.addMovementDesire(new DesireOfferFlower(this.getRemoteEntity()), 5);
			mind.addMovementDesire(new DesireWanderAround(this.getRemoteEntity()), 6);
			mind.addMovementDesire(new DesireLookAtNearest(this.getRemoteEntity(), EntityHuman.class, 6), 7);
			mind.addMovementDesire(new DesireLookRandomly(this.getRemoteEntity()), 8);
			mind.addActionDesire(new DesireDefendVillage(this.getRemoteEntity()), 1);
			mind.addActionDesire(new DesireAttackTarget(this.getRemoteEntity(), 16, false, false), 2);
			mind.addActionDesire(new DesireAttackNearest(this.getRemoteEntity(), EntityMonster.class, 16, false, true, 0), 3);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
		this.getRemoteEntity().getMind().tick();
	}
	
	@Override
	public void b_(EntityHuman entity)
	{
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
	public boolean c(EntityHuman entity)
	{
		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Interact"))
		{
			((InteractBehavior)this.getRemoteEntity().getMind().getBehaviour("Interact")).onInteract((Player)entity.getBukkitEntity());
		}
		
		return super.c(entity);
	}
	
	@Override
	public void die(DamageSource damagesource)
	{
		this.getRemoteEntity().getMind().clearMovementDesires();
		this.getRemoteEntity().getMind().clearActionDesires();
		super.die(damagesource);
	}
}
