package de.kumpelblase2.remoteentities.entities;

import java.util.*;
import net.minecraft.server.v1_6_R3.*;
import net.minecraft.server.v1_6_R3.World;
import org.bukkit.*;
import org.bukkit.Chunk;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.*;
import de.kumpelblase2.remoteentities.api.features.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.nms.RemoteSpeedModifier;
import de.kumpelblase2.remoteentities.persistence.ISingleEntitySerializer;
import de.kumpelblase2.remoteentities.utilities.*;

public abstract class RemoteBaseEntity<T extends LivingEntity> implements RemoteEntity
{
	private final int m_id;
	protected Mind m_mind;
	protected FeatureSet m_features;
	protected boolean m_isStationary = false;
	protected final RemoteEntityType m_type;
	protected EntityLiving m_entity;
	protected boolean m_isPushable = true;
	protected final EntityManager m_manager;
	protected Location m_unloadedLocation;
	protected String m_nameToSpawnwith;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	protected double m_speed = -1;
	protected AttributeModifier m_speedModifier;
	protected Map<EntitySound, Object> m_sounds;

	public RemoteBaseEntity(int inID, RemoteEntityType inType, EntityManager inManager)
	{
		this.m_id = inID;
		this.m_mind = new Mind(this);
		this.m_features = new FeatureSet(this);
		this.m_type = inType;
		this.m_manager = inManager;
		this.m_sounds = new EnumMap<EntitySound, Object>(EntitySound.class);
		this.setupSounds();
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
	public boolean move(Location inLocation, double inSpeed)
	{
		if(!this.isSpawned() || this.m_isStationary || NMSUtil.isOnLeash(this.getHandle()))
			return false;

		if(!NMSUtil.getNavigation(this.m_entity).a(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inSpeed))
		{
			PathEntity path = this.m_entity.world.a(this.getHandle(), MathHelper.floor(inLocation.getX()), (int) inLocation.getY(), MathHelper.floor(inLocation.getZ()), (float)this.getPathfindingRange(), true, false, false, true);
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
	public boolean move(LivingEntity inEntity, double inSpeed)
	{
		if(!this.isSpawned() || this.m_isStationary || NMSUtil.isOnLeash(this.getHandle()))
			return false;

		EntityLiving handle = ((CraftLivingEntity)inEntity).getHandle();
		if(handle == this.m_entity)
			return true;

		if(!NMSUtil.getNavigation(this.m_entity).a(handle, inSpeed))
		{
			PathEntity path = this.m_entity.world.findPath(this.getHandle(), handle, (float)this.getPathfindingRange(), true, false, false, true);
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
			this.m_entity.aO = inYaw;
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

		this.m_entity.aP = inHeadYaw;
		this.m_entity.aQ = inHeadYaw;
		if(!(this.m_entity instanceof EntityHuman))
			this.m_entity.aN = inHeadYaw;
	}

	@Override
	public void lookAt(Location inLocation)
	{
		if(!this.isSpawned())
			return;

		NMSUtil.getControllerLook(this.m_entity).a(inLocation.getX(), inLocation.getY(), inLocation.getZ(), 10, NMSUtil.getMaxHeadRotation(this.m_entity));
	}

	@Override
	public void lookAt(Entity inEntity)
	{
		if(!this.isSpawned())
			return;

		NMSUtil.getControllerLook(this.m_entity).a(((CraftEntity)inEntity).getHandle(), 10, NMSUtil.getMaxHeadRotation(this.m_entity));
	}

	@Override
	public void stopMoving()
	{
		if(this.m_entity == null)
			return;

		NMSUtil.getNavigation(this.m_entity).h();
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
			this.m_entity = this.m_type.getEntityClass().getConstructor(World.class, RemoteEntity.class).newInstance(worldServer, this);
			this.m_entity.setPositionRotation(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inLocation.getYaw(), inLocation.getPitch());
			worldServer.addEntity(this.m_entity, SpawnReason.CUSTOM);
			entry.restore();
			this.getBukkitEntity().setMetadata("remoteentity", new FixedMetadataValue(this.m_manager.getPlugin(), this));
			if(this.getName() != null)
			{
				this.getBukkitEntity().setCustomName(this.getName());
				this.getBukkitEntity().setCustomNameVisible(true);
			}

			if(!inLocation.getBlock().getRelative(BlockFace.DOWN).isEmpty())
				this.m_entity.onGround = true;

			if(this.m_speed != -1)
				this.setSpeed(this.m_speed);

			if(this.m_speedModifier != null)
			{
				this.addSpeedModifier(this.m_speedModifier.d(), (this.m_speedModifier.c() == 0));
				this.m_speedModifier = null;
			}
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

		if(inReason != DespawnReason.CHUNK_UNLOAD && inReason != DespawnReason.NAME_CHANGE)
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
	public double getSpeed()
	{
		if(this.m_entity == null)
		{
			if(this.m_speed != -1)
				return this.m_speed;
			else
				return GenericAttributes.d.b();
		}
		else
			return this.m_entity.getAttributeInstance(GenericAttributes.d).getValue();
	}

	@Override
	public void setSpeed(double inSpeed)
	{
		if(this.m_entity == null)
			this.m_speed = inSpeed;
		else
			this.m_entity.getAttributeInstance(GenericAttributes.d).setValue(inSpeed);
	}

	@Override
	public void removeSpeedModifier()
	{
		if(this.m_entity != null)
			this.m_entity.getAttributeInstance(GenericAttributes.d).b(new RemoteSpeedModifier(0, false));
	}

	@Override
	public void addSpeedModifier(double inAmount, boolean inAdditive)
	{
		RemoteSpeedModifier modifier = new RemoteSpeedModifier(inAmount, inAdditive);
		if(this.m_entity == null)
			this.m_speedModifier = modifier;
		else
		{
			AttributeInstance instance = this.m_entity.getAttributeInstance(GenericAttributes.d);
			instance.b(modifier);
			instance.a(modifier);
		}
	}

	@Override
	public void setPathfindingRange(double inRange)
	{
		this.m_entity.getAttributeInstance(GenericAttributes.b).setValue(inRange);
	}

	@Override
	public double getPathfindingRange()
	{
		return this.m_entity.getAttributeInstance(GenericAttributes.b).getValue();
	}

	/**
	 * Sets the path of the entity with a given speed.
	 *
	 * @param inPath	Path to follow
	 * @param inSpeed	Speed to walk with
	 * @return			true if it could use the path, false if not
	 */
	public boolean moveWithPath(PathEntity inPath, double inSpeed)
	{
		if(this.m_entity == null || inPath == null || this.m_isStationary)
			return false;

		if(this.m_entity instanceof EntityCreature)
			((EntityCreature)this.m_entity).setPathEntity(inPath);

		return NMSUtil.getNavigation(this.m_entity).a(inPath, inSpeed);
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

	@Override
	public String getSound(EntitySound inType)
	{
		Object sound = this.m_sounds.get(inType);
		if(sound instanceof String)
			return (String)sound;
		else
		{
			Random generator = new Random();
			Object[] values = this.m_sounds.values().toArray();
			return (String)values[generator.nextInt(values.length)];
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSound(EntitySound inType, String inKey)
	{
		Object sounds = this.m_sounds.get(inType);
		if(!(sounds instanceof Map))
			return null;

		return ((Map<String, String>)sounds).get(inKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getSounds(EntitySound inType)
	{
		Object sounds = this.m_sounds.get(inType);
		if(sounds instanceof String)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("default", (String)sounds);
			return map;
		}
		else
		{
			return (Map<String, String>)sounds;
		}
	}

	@Override
	public boolean hasSound(EntitySound inType)
	{
		return this.getSound(inType) != null;
	}

	@Override
	public boolean hasSound(EntitySound inType, String inKey)
	{
		return this.getSound(inType, inKey) != null;
	}

	@Override
	public void setSound(EntitySound inType, String inSound)
	{
		this.m_sounds.put(inType, inSound);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSound(EntitySound inType, String inKey, String inSound)
	{
		Object sounds = this.m_sounds.get(inType);
		if(sounds instanceof String)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put(inKey, inSound);
			this.m_sounds.put(inType, map);
		}
		else
			((Map<String, String>)sounds).put(inKey, inSound);
	}

	@Override
	public void setSounds(EntitySound inType, Map<String, String> inSounds)
	{
		this.m_sounds.put(inType, inSounds);
	}

	public String getName()
	{
		if(!this.isSpawned())
			return this.m_nameToSpawnwith;

		return this.getBukkitEntity().getCustomName();
	}

	public void setName(String inName)
	{
		if(this.isSpawned())
		{
			if(inName == null)
			{
				this.getBukkitEntity().setCustomNameVisible(false);
				this.getBukkitEntity().setCustomName(null);
			}
			else
			{
				this.getBukkitEntity().setCustomNameVisible(true);
				this.getBukkitEntity().setCustomName(inName);
			}
		}
		else
			this.m_nameToSpawnwith = inName;
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

	Vector onPush(double inX, double inY, double inZ)
	{
		RemoteEntityPushEvent event = new RemoteEntityPushEvent(this, new Vector(inX, inY, inZ));
		event.setCancelled(!this.isPushable() || this.isStationary());
		Bukkit.getPluginManager().callEvent(event);

		if(!event.isCancelled())
			return event.getVelocity();
		else
			return null;
	}

	boolean onCollide(Entity inEntity)
	{
		if(this.getMind() == null)
			return true;

		if(this.m_lastBouncedId != inEntity.getEntityId() || System.currentTimeMillis() - this.m_lastBouncedTime > 1000)
		{
			RemoteEntityTouchEvent event = new RemoteEntityTouchEvent(this, inEntity);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return false;

			if(inEntity instanceof Player && this.getMind().canFeel() && this.getMind().hasBehaviour("Touch"))
			{
				if(inEntity.getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
					((TouchBehavior)this.getMind().getBehaviour("Touch")).onTouch((Player)inEntity);
			}
		}

		this.m_lastBouncedTime = System.currentTimeMillis();
		this.m_lastBouncedId = inEntity.getEntityId();
		return true;
	}

	void onDeath()
	{
		this.getMind().clearMovementDesires();
		this.getMind().clearTargetingDesires();
	}

	boolean onInteract(Player inEntity, boolean inLeftClick)
	{
		if(this.getFeatures().hasFeature(TradingFeature.class))
		{
			TradingFeature feature = this.getFeatures().getFeature(TradingFeature.class);
			feature.openFor(inEntity);
			return false;
		}

		if(this.getMind() == null)
			return true;

		if(this.getMind().canFeel())
		{
			RemoteEntityInteractEvent event = new RemoteEntityInteractEvent(this, inEntity, inLeftClick);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return false;

			if(this.getMind().hasBehaviour("Interact"))
				((InteractBehavior)this.getMind().getBehaviour("Interact")).onInteract(inEntity);
		}
		return true;
	}

	boolean onInteract(Player inEntity)
	{
		return this.onInteract(inEntity, true);
	}

	protected abstract void setupSounds();
}