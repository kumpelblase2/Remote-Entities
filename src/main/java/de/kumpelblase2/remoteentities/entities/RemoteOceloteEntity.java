package de.kumpelblase2.remoteentities.entities;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.Item;
import net.minecraft.server.World;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.api.thinking.PathfinderGoalSelectorHelper;
import de.kumpelblase2.remoteentities.api.thinking.TouchBehaviour;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteOceloteEntity extends EntityOcelot implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected int m_maxHealth;
	public static int defaultMaxHealth = 10;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	
	static
	{
		ReflectionUtil.registerEntityType(RemoteOceloteEntity.class, "Ozelote", 98);
	}
	
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
		this.m_maxHealth = defaultMaxHealth;
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
			mind.addActionDesire(new DesireNonTamedAttackNearest(this.getRemoteEntity(), EntityChicken.class, 14, false, true, 750), 1);
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
					
					((TouchBehaviour)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch((Player)entity.getBukkitEntity());
					this.m_lastBouncedTime = System.currentTimeMillis();
					this.m_lastBouncedId = entity.id;
				}
			}
		}
		super.b_(entity);
	}
	
	@Override
	public void die(DamageSource damagesource)
	{
		this.getRemoteEntity().getMind().clearMovementDesires();
		this.getRemoteEntity().getMind().clearActionDesires();
		super.die(damagesource);
	}
}
