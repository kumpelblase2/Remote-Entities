package de.kumpelblase2.remoteentities.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.InteractBehaviour;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.api.thinking.PathfinderGoalSelectorHelper;
import de.kumpelblase2.remoteentities.api.thinking.TouchBehaviour;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.World;

public class RemoteZombieEntity extends EntityZombie implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected final PathfinderGoalSelectorHelper goalSelectorHelper;
	protected final PathfinderGoalSelectorHelper targetSelectorHelper;
	protected int m_maxHealth;
	public static int defaultMaxHealth = 20;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	
	static
	{
		ReflectionUtil.registerEntityType(RemoteZombieEntity.class, "Zombie", 54);
	}
	
	public RemoteZombieEntity(World world)
	{
		this(world, null);
	}
	
	public RemoteZombieEntity(World world, RemoteEntity inEntity)
	{
		super(world);
		this.m_remoteEntity = inEntity;
		this.goalSelectorHelper = new PathfinderGoalSelectorHelper(this.goalSelector);
		this.targetSelectorHelper = new PathfinderGoalSelectorHelper(this.targetSelector);
		this.m_maxHealth = defaultMaxHealth;
		this.goalSelectorHelper.clearGoals();
		this.targetSelectorHelper.clearGoals();
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
			mind.addMovementDesire(new DesireDestroyDoor(this.getRemoteEntity(), false), 1);
			mind.addMovementDesire(new DesireAttackOnCollide(this.getRemoteEntity(), EntityHuman.class, false), 2);
			mind.addMovementDesire(new DesireAttackOnCollide(this.getRemoteEntity(), EntityVillager.class, true), 3);
			mind.addMovementDesire(new DesireMoveTowardsRestriction(this.getRemoteEntity()), 4);
			mind.addMovementDesire(new DesireMoveThroughVillage(this.getRemoteEntity(), false), 5);
			mind.addMovementDesire(new DesireWanderAround(this.getRemoteEntity()), 6);
			mind.addMovementDesire(new DesireLookAtNearest(this.getRemoteEntity(), EntityHuman.class, 8), 7);
			mind.addMovementDesire(new DesireLookRandomly(this.getRemoteEntity()), 7);
			mind.addActionDesire(new DesireAttackTarget(this.getRemoteEntity(), 16, false, false), 1);
			mind.addActionDesire(new DesireAttackNearest(this.getRemoteEntity(), EntityHuman.class, 16, false, true, 0), 2);
			mind.addActionDesire(new DesireAttackNearest(this.getRemoteEntity(), EntityVillager.class, 16, false, true, 0), 2);
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
	public boolean bb()
	{
		return true;
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
		if(entity instanceof EntityPlayer)
		{
			if (this.getRemoteEntity().getMind().canFeel() && (this.m_lastBouncedId != entity.id || System.currentTimeMillis() - this.m_lastBouncedTime > 1000) && this.getRemoteEntity().getMind().hasBehaviour("Touch")) {
				if(entity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
				{
					RemoteEntityTouchEvent event = new RemoteEntityTouchEvent(this.m_remoteEntity, entity.getBukkitEntity());
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())
						return;
					
					((TouchBehaviour)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch((Player)entity.getBukkitEntity());
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
			((InteractBehaviour)this.getRemoteEntity().getMind().getBehaviour("Interact")).onInteract((Player)entity.getBukkitEntity());
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