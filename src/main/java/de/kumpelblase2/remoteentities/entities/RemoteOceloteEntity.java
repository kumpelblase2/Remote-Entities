package de.kumpelblase2.remoteentities.entities;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.InteractBehaviour;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.api.thinking.PathfinderGoalSelectorHelper;
import de.kumpelblase2.remoteentities.api.thinking.TouchBehaviour;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireAttackNearest;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireAttackOnCollide;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireAttackTarget;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireAvoidSpecific;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireBreed;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireDefendVillage;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireFollowTamer;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLeapAtTarget;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookAtNearest;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookRandomly;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireMoveThroughVillage;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireMoveToTarget;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireMoveTowardsRestriction;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireOcelotAttack;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireOfferFlower;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSit;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSitOnBlock;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSwell;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSwim;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireTempt;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireWanderAround;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteOceloteEntity extends EntityOcelot implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected final PathfinderGoalSelectorHelper goalSelectorHelper;
	protected final PathfinderGoalSelectorHelper targetSelectorHelper;
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
		this.goalSelectorHelper = new PathfinderGoalSelectorHelper(this.goalSelector);
		this.targetSelectorHelper = new PathfinderGoalSelectorHelper(this.targetSelector);
		this.m_maxHealth = defaultMaxHealth;
	}
	
	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature("Inventory"))
			return null;
		
		return ((InventoryFeature)this.m_remoteEntity.getFeatures().getFeature("Inventory")).getInventory();
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
			mind.addActionDesire(new DesireDefendVillage(this.getRemoteEntity()), 1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public PathfinderGoalSelectorHelper getGoalSelector()
	{
		return this.goalSelectorHelper;
	}

	@Override
	public PathfinderGoalSelectorHelper getTargetSelector()
	{
		return this.targetSelectorHelper;
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
	public void h_()
	{
		super.h_();
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
	public boolean c(EntityHuman entityhuman)
	{
		if(entityhuman instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Interact"))
		{
			((InteractBehaviour)this.getRemoteEntity().getMind().getBehaviour("Interact")).onInteract((Player)entityhuman.getBukkitEntity());
		}
		
		ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (this.isTamed()) {
            if (entityhuman.name.equalsIgnoreCase(this.getOwnerName()) && !this.world.isStatic && !this.b(itemstack)) {
                this.d.a(!this.isSitting());
            }
        } else if (this.getRemoteEntity().getMind().getMovementDesire(DesireTempt.class).isTempted() && itemstack != null && itemstack.id == Item.RAW_FISH.id && entityhuman.e(this) < 9.0D) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
            }

            if (itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
            }

            if (!this.world.isStatic) {
                if (this.random.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.setCatType(1 + this.world.random.nextInt(3));
                    this.setOwnerName(entityhuman.name);
                    this.e(true);
                    this.d.a(true);
                    this.world.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.e(false);
                    this.world.broadcastEntityEffect(this, (byte) 6);
                }
            }

            return true;
        }

        if (itemstack != null && this.b(itemstack) && this.getAge() == 0) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
                if (itemstack.count <= 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }
            }

            try
            {
            	Field loveField = EntityAnimal.class.getDeclaredField("love");
            	loveField.setAccessible(true);
            	loveField.set(this, 600);
            }
            catch(Exception e)
            {
            }
            this.target = null;

            for (int i = 0; i < 7; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.world.a("heart", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + 0.5D + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }

            return true;
        } else {
            return false;
        }
	}
}
