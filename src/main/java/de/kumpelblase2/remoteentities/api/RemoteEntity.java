package de.kumpelblase2.remoteentities.api;

import net.minecraft.server.v1_5_R2.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.features.FeatureSet;
import de.kumpelblase2.remoteentities.api.thinking.Mind;

public interface RemoteEntity
{
	/**
	 * Gets the ID of the entity
	 * 
	 * @return ID
	 */
	public int getID();
	
	/**
	 * Gets the type of the entity
	 * 
	 * @return	type
	 */
	public RemoteEntityType getType();
	
	/**
	 * Gets the mind of the entity
	 * 
	 * @return	mind
	 */
	public Mind getMind();
	
	/**
	 * Gets the bukkit entity of this RemoteEntity
	 * 
	 * @return	bukkit entity
	 */
	public LivingEntity getBukkitEntity();
	
	/**
	 * Gets the native minecraft entity
	 * 
	 * @return	native entity
	 */
	public EntityLiving getHandle();
	
	/**
	 * Gets the features of this entity
	 * 
	 * @return features
	 */
	public FeatureSet getFeatures();
	
	/**
	 * Tries to move the entity to a location
	 * 
	 * @param inLocation	location to move to
	 * @return				true if it was possible to move the entity, false if not
	 */
	public boolean move(Location inLocation);
	
	/**
	 * Tries to move the entity to a location with given speed
	 * 
	 * @param inLocation	location to move to
	 * @param inSpeed		speed of the entity
	 * @return				true if it was possible, false if not
	 */
	public boolean move(Location inLocation, float inSpeed);
	
	/**
	 * Tries to move the entity towards another entity
	 * 
	 * @param inEntity	entity to move to
	 * @return			true if it was possible to move the entity, false if not
	 */
	public boolean move(LivingEntity inEntity);
	
	/**
	 * Tries to move the entity towards another entity with given speed
	 * 
	 * @param inEntity	entity to move to
	 * @param inSpeed	speed of the entity
	 * @return			true if it was possible, false if not
	 */
	public boolean move(LivingEntity inEntity, float inSpeed);
	
	/**
	 * Sets that yaw of the entity
	 * When the entity is stationary, the yaw value will be fixed to the new value.
	 * 
	 * @param inYaw	new yaw
	 */
	public void setYaw(float inYaw);
	
	/**
	 * When inRotate is true, it will smoothly rotate to the new yaw rotation.
	 * Otherwise it will just set the new value.
	 * When the entity is stationary, the yaw value will be fixed to the new value.
	 * 
	 * @param inYaw		new yaw
	 * @param inRotate	If the entity should rotate or snap to the new value
	 */
	public void setYaw(float inYaw, boolean inRotate);
	
	/**
	 * Sets the head pitch of the entity
	 * When the entity is stationary, the pitch value will be fixed to the new value.
	 * 
	 * @param inPitch	new pitch
	 */
	public void setPitch(float inPitch);
	
	/**
	 * Sets that yaw of the head of the entity
	 * When the entity is stationary, the yaw value will be fixed to the new value.
	 * 
	 * @param inHeadYaw	new head yaw
	 */
	public void setHeadYaw(float inHeadYaw);

	/**
	 * Lets the entity look at the given location
	 * 
	 * @param inLocation	location to look at
	 */
	public void lookAt(Location inLocation);
	
	/**
	 * Lets the entity look at another entity
	 * 
	 * @param inEntity	entity to look at
	 */
	public void lookAt(Entity inEntity);
	
	/**
	 * Stops the current movement of the entity
	 */
	public void stopMoving();
	
	/**
	 * Teleports the entity to a location
	 * 
	 * @param inLocation location to teleport to
	 */
	public void teleport(Location inLocation);
	
	/**
	 * Spawns the entity at a location
	 * 
	 * @param inLocation location to spawn at
	 */
	public void spawn(Location inLocation);
	
	/**
	 * Spawns the entity at a location even when the chunk is not loaded
	 * 
	 * @param inLocation	location to spawn at
	 * @param inForce		if the spawn should be forced on a non-loaded chunk or not
	 */
	public void spawn(Location inLocation, boolean inForce);
	
	/**
	 * Despawns the entity with a specific reason
	 * 
	 * @param inReason	reason for despawning
	 */
	public boolean despawn(DespawnReason inReason);
	
	/**
	 * Checks if the entity is spawned
	 * 
	 * @return true of spawned, false if not
	 */
	public boolean isSpawned();
	
	/**
	 * Sets the the stationary state of the entity.
	 * While being stationary an entity is unable to move.
	 * This will also reset the fixed yaw and pitch rotation
	 * 
	 * @param inState	stationary state
	 */
	public void setStationary(boolean inState);
	
	/**
	 * Sets the stationary state of the entity.
	 * While being stationary an entity is unable to move.
	 * 
	 * @param inState			stationary state
	 * @param inKeepHeadFixed	Determines whether the entity should keep its fixed yaw and pitch when changing its state
	 */
	public void setStationary(boolean inState, boolean inKeepHeadFixed);
	
	/**
	 * Gets if the entity is stationary
	 * 
	 * @return true if stationary, false if not
	 */
	public boolean isStationary();
	
	/**
	 * Gets the movement speed of the entity
	 * 
	 * @return speed
	 */
	public float getSpeed();
	
	/**
	 * Sets the speed of the entity
	 * 
	 * @param inSpeed speed
	 */
	public void setSpeed(float inSpeed);
	
	/**
	 * Gets if the entity is pushable or not
	 * 
	 * @return true if pushable, false if not
	 */
	public boolean isPushable();
	
	/**
	 * Sets if the entity is pushable or not
	 * 
	 * @param inState pushable status
	 */
	public void setPushable(boolean inState);
	
	/**
	 * Gets the manager that created this entity
	 * 
	 * @return EntityManager
	 */
	public EntityManager getManager();
	
	/**
	 * The native name is the name that is used in the EntityTypes enum. This is used internally for automatically registering the entity to the enum.
	 * 
	 * @return native name
	 */
	public String getNativeEntityName();
	
	/**
	 * If a ISingleEntitySerializer is present, this entity will be saved alone.
	 * 
	 * @return	true if the entity could get saved, false if not
	 */
	public boolean save();
}
