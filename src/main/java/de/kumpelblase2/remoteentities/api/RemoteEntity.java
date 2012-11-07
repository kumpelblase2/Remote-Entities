package de.kumpelblase2.remoteentities.api;

import net.minecraft.server.EntityLiving;
import org.bukkit.Location;
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
	 * Tries to move the entity towards an entity
	 * 
	 * @param inEntity	entity to move to
	 * @return			true if it was possible to move the entity, false if not
	 */
	public boolean move(LivingEntity inEntity);
	
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
	 * Despawns the entity with a specific reason
	 * 
	 * @param inReason	reason for despawning
	 */
	public void despawn(DespawnReason inReason);
	
	/**
	 * Checks if the entity is spawned
	 * 
	 * @return true of spawned, false if not
	 */
	public boolean isSpawned();
	
	/**
	 * Sets the maximum health
	 * 
	 * @param inMax max health
	 */
	public void setMaxHealth(int inMax);
	
	/**
	 * Gets the maximum health
	 * 
	 * @return max health
	 */
	public int getMaxHealth();
	
	/**
	 * Sets the the stationary state of the entity.
	 * While being stationary an entity is unable to move
	 * 
	 * @param inState	stationary state
	 */
	public void setStationary(boolean inState);
	
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
}
