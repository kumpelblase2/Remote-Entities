package de.kumpelblase2.remoteentities.entities;

import java.lang.reflect.Field;
import net.minecraft.server.v1_5_R3.*;
import net.minecraft.server.v1_5_R3.World;
import org.bukkit.*;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityDespawnEvent;
import de.kumpelblase2.remoteentities.api.events.RemoteEntitySpawnEvent;
import de.kumpelblase2.remoteentities.api.features.FeatureSet;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.Behavior;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.persistence.ISingleEntitySerializer;
import de.kumpelblase2.remoteentities.utilities.EntityTypesEntry;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public abstract class RemoteBaseEntity<T extends LivingEntity> implements RemoteEntity
{
	private final int m_id;
	protected Mind m_mind;
	protected FeatureSet m_features;
	protected boolean m_isStationary = false;
	protected final RemoteEntityType m_type;
	protected EntityLiving m_entity;
	protected boolean m_isPushable = true;
	protected float m_speed;
	protected final EntityManager m_manager;
	protected Location m_unloadedLocation;
	
	public RemoteBaseEntity(int inID, RemoteEntityType inType, EntityManager inManager)
	{
		this.m_id = inID;
		this.m_mind = new Mind(this);
		this.m_features = new FeatureSet();
		this.m_type = inType;
		try
		{
			Field speed = DefaultEntitySpeed.class.getDeclaredField(this.m_type.name().toUpperCase() + "_SPEED");
			this.m_speed = speed.getFloat(null);
		}
		catch(Exception e)
		{
			this.m_speed = 0.25F;
		}
		this.m_manager = inManager;
	}

	@Override
	public int getID()
	{
		return this.m_id;
	}

	@Override
	public EntityManager getManager()
	{
		return this.m_manager;
	}
	
	@Override
	public Mind getMind()
	{
		return this.m_mind;
	}

	@Override
	public FeatureSet getFeatures()
	{
		return this.m_features;
	}

	@Override
	public void setStationary(boolean inState)
	{
		this.setStationary(inState, false);
	}
	
	@Override
	public void setStationary(boolean inState, boolean inKeepHeadFixed)
	{
		this.m_isStationary = inState;
		if(!inKeepHeadFixed)
		{
			this.getMind().resetFixedYaw();
			this.getMind().resetFixedPitch();
		}
	}

	@Override
	public boolean isStationary()
	{
		return this.m_isStationary;
	}

	@Override
	public RemoteEntityType getType()
	{
		return this.m_type;
	}

	@Override
	public EntityLiving getHandle()
	{
		return this.m_entity;
	}
	
	@Override
	public T getBukkitEntity()
	{
		if(this.isSpawned())
			return (T)this.m_entity.getBukkitEntity();
		
		return null;
	}
	
	@Override
	public boolean move(Location inLocation)
	{
		return this.move(inLocation, this.getSpeed());
	}
	
	@Override
	public boolean move(Location inLocation, float inSpeed)
	{
		if(!this.isSpawned() || this.m_isStationary)
			return false;

		if(!this.m_entity.getNavigation().a(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inSpeed))
		{
			PathEntity path = this.m_entity.world.a(this.getHandle(), MathHelper.floor(inLocation.getX()), (int) inLocation.getY(), MathHelper.floor(inLocation.getZ()), 20, true, false, false, true);
			return this.moveWithPath(path, inSpeed);
		}
		return true;
	}
	
	@Override
	public boolean move(LivingEntity inEntity)
	{
		return this.move(inEntity, this.getSpeed());
	}
	
	@Override
	public boolean move(LivingEntity inEntity, float inSpeed)
	{
		if(!this.isSpawned() || this.m_isStationary)
			return false;

		EntityLiving handle = ((CraftLivingEntity)inEntity).getHandle();
		if(handle == this.m_entity)
			return true;

		if(!this.m_entity.getNavigation().a(handle, inSpeed))
		{
			PathEntity path = this.m_entity.world.findPath(this.getHandle(), handle, 20, true, false, false, true);
			return this.moveWithPath(path, inSpeed);
		}
		return true;
	}
	
	@Override
	public void setYaw(float inYaw)
	{
		this.setYaw(inYaw, false);
	}
	
	@Override
	public void setYaw(float inYaw, boolean inRotate)
	{
		if(!this.isSpawned())
			return;
		
		Location newLoc = this.getBukkitEntity().getLocation();
		newLoc.setYaw(inYaw);
		if(inRotate)
			this.move(newLoc);
		else
		{
			if(this.isStationary())
				this.getMind().fixYawAt(inYaw);
			
			this.m_entity.yaw = inYaw;
			this.m_entity.az = inYaw;
		}
	}
	
	@Override
	public void setPitch(float inPitch)
	{
		if(!this.isSpawned())
			return;
		
		if(this.isStationary())
			this.getMind().fixPitchAt(inPitch);
		
		this.m_entity.pitch = inPitch;
	}
	
	@Override
	public void setHeadYaw(float inHeadYaw)
	{
		if(!this.isSpawned())
			return;
		
		if(this.isStationary())
			this.getMind().fixHeadYawAt(inHeadYaw);
			
		this.m_entity.aA = inHeadYaw;
		this.m_entity.aB = inHeadYaw;
	}

	@Override
	public void lookAt(Location inLocation)
	{
		if(!this.isSpawned())
			return;
		
		this.m_entity.getControllerLook().a(inLocation.getX(), inLocation.getY(), inLocation.getZ(), 10, this.m_entity.bs());
	}
	
	@Override
	public void lookAt(Entity inEntity)
	{
		if(!this.isSpawned())
			return;
		
		this.m_entity.getControllerLook().a(((CraftEntity)inEntity).getHandle(), 10, this.m_entity.bs());
	}
	
	@Override
	public void stopMoving()
	{
		if(this.m_entity == null)
			return;
		
		this.getHandle().getNavigation().g();
	}

	@Override
	public void teleport(Location inLocation)
	{
		if(this.m_entity == null)
			return;
		
		this.getBukkitEntity().teleport(inLocation);
	}
	
	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;
		
		RemoteEntitySpawnEvent event = new RemoteEntitySpawnEvent(this, inLocation);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		inLocation = event.getSpawnLocation();
		
		try
		{
			EntityTypesEntry entry = EntityTypesEntry.fromEntity(this.getNativeEntityName());
			ReflectionUtil.registerEntityType(this.getType().getEntityClass(), this.getNativeEntityName(), entry.getID());
			WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
			this.m_entity = this.m_type.getEntityClass().getConstructor(World.class, de.kumpelblase2.remoteentities.api.RemoteEntity.class).newInstance(worldServer, this);
			this.m_entity.setPositionRotation(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inLocation.getYaw(), inLocation.getPitch());
			worldServer.addEntity(this.m_entity, SpawnReason.CUSTOM);
			entry.restore();
			this.getBukkitEntity().setMetadata("remoteentity", new FixedMetadataValue(this.m_manager.getPlugin(), this));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.m_unloadedLocation = null;
	}
	
	@Override
	public void spawn(Location inLocation, boolean inForce)
	{
		Chunk c = inLocation.getChunk();
		if(c == null)
			return;
		
		if(!c.isLoaded() && inForce)
		{
			if(!c.load())
				return;
		}
		
		this.spawn(inLocation);
	}
	
	@Override
	public boolean despawn(DespawnReason inReason)
	{		
		RemoteEntityDespawnEvent event = new RemoteEntityDespawnEvent(this, inReason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled() && inReason != DespawnReason.PLUGIN_DISABLE)
			return false;
		
		if(inReason != DespawnReason.CHUNK_UNLOAD)
		{
			for(Behavior behaviour : this.getMind().getBehaviours())
			{
				behaviour.onRemove();
			}
			
			this.getMind().clearBehaviours();
		}
		else
			this.m_unloadedLocation = (this.getBukkitEntity() != null ? this.getBukkitEntity().getLocation() : null);
		
		if(this.getBukkitEntity() != null)
			this.getBukkitEntity().remove();
		this.m_entity = null;
		return true;
	}
	
	@Override
	public boolean isSpawned()
	{
		return this.m_entity != null;
	}
	
	@Override
	public boolean isPushable()
	{
		return this.m_isPushable;
	}

	@Override
	public void setPushable(boolean inState)
	{
		this.m_isPushable = inState;
	}
	
	@Override
	public float getSpeed()
	{
		return this.m_speed;
	}
	
	@Override
	public void setSpeed(float inSpeed)
	{
		this.m_speed = inSpeed;
	}
	
	/**
	 * Sets the path of the entity with a given speed.
	 * 
	 * @param inPath	Path to follow
	 * @param inSpeed	Speed to walk with
	 * @return			true if it could use the path, false if not
	 */
	public boolean moveWithPath(PathEntity inPath, float inSpeed)
	{
		if(this.m_entity == null || inPath == null || this.m_isStationary)
			return false;
		
		if(this.m_entity instanceof EntityCreature)
			((EntityCreature)this.m_entity).setPathEntity(inPath);
		
		return this.m_entity.getNavigation().a(inPath, inSpeed);
	}
	
	/**
	 * Copies the inventory from the given player to the inventory of this entity.
	 * 
	 * @param inPlayer	Player to copy inventory from
	 */
	public void copyInventory(Player inPlayer)
	{
		this.copyInventory(inPlayer, false);
	}
	
	/**
	 * Copies the inventory from the given player to the inventory of this entity.
	 * 
	 * @param inPlayer			Player to copy inventory from
	 * @param inIgnoreArmor		If armor should not be copied or if it should
	 */
	public void copyInventory(Player inPlayer, boolean inIgnoreArmor)
	{
		this.copyInventory(inPlayer.getInventory());
		EntityEquipment equip = this.getBukkitEntity().getEquipment();
		if(!inIgnoreArmor)
			equip.setArmorContents(inPlayer.getInventory().getArmorContents());
		
		if(this.getInventory() instanceof CraftInventoryPlayer)
			((CraftInventoryPlayer)this.getInventory()).setHeldItemSlot(inPlayer.getInventory().getHeldItemSlot());
		else
			equip.setItemInHand(inPlayer.getItemInHand());		
	}
	
	/**
	 * Copies the the contents of the given inventory to the inventory of this entity.
	 * 
	 * @param inInventory	Inventory to copy from.
	 */
	public void copyInventory(Inventory inInventory)
	{
		if(this.getInventory() != null)
			this.getInventory().setContents(inInventory.getContents());
	}
	
	/**
	 * Gets the inventory of this entity.
	 * 
	 * @return	Inventory
	 */
	public Inventory getInventory()
	{
		if(this.getHandle() instanceof RemoteEntityHandle)
			return ((RemoteEntityHandle)this.getHandle()).getInventory();
		
		if(!this.getFeatures().hasFeature(InventoryFeature.class))
			return null;
		
		return this.getFeatures().getFeature(InventoryFeature.class).getInventory();
	}

	@Override
	public boolean save()
	{
		if(this.getManager().getSerializer() instanceof ISingleEntitySerializer)
		{
			ISingleEntitySerializer serializer = (ISingleEntitySerializer)this.getManager().getSerializer();
			serializer.save(serializer.prepare(this));
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the location the entity was last unloaded.
	 * 
	 * @return	unloading location
	 */
	public Location getUnloadedLocation()
	{
		return this.m_unloadedLocation;
	}
}