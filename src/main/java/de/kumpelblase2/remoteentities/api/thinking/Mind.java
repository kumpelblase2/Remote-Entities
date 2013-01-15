package de.kumpelblase2.remoteentities.api.thinking;

import java.util.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class Mind
{
	private Map<String, Behavior> m_behaviours;
	private RemoteEntity m_entity;
	private boolean m_canFeel = true;
	private Navigation m_targetNavigation;
	private Navigation m_movementNavigation;
	private boolean changedYaw = false, changedPitch = false;
	private float yaw, pitch;
	
	public Mind(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_behaviours = new HashMap<String, Behavior>();
		this.m_targetNavigation = new Navigation();
		this.m_movementNavigation = new Navigation();
	}
	
	/**
	 * Adds a behavior to the entity
	 * 
	 * @param inBehaviour behavior
	 */
	public void addBehaviour(Behavior inBehaviour)
	{
		this.m_behaviours.put(inBehaviour.getName(), inBehaviour);
	}
	
	/**
	 * Removes a behavior from the entity with given name
	 * 
	 * @param inName	name
	 * @return			true if behavior got removed, false if not
	 */
	public boolean removeBehaviour(String inName)
	{
		return this.m_behaviours.remove(inName) != null;
	}
	
	/**
	 * Checks if the entity has a specific behavior
	 * 
	 * @param inName 	name
	 * @return			true if the entity has such behavior, false if not
	 */
	public boolean hasBehaviour(String inName)
	{
		return this.m_behaviours.containsKey(inName);
	}
	
	/**
	 * Checks if the entity can currently feel
	 * 
	 * @return true if the entity can, false if not
	 */
	public boolean canFeel()
	{
		return this.m_canFeel;
	}
	
	/**
	 * Blocks or unblocks the feelings of the entity
	 * 
	 * @param inState blocking state
	 */
	public void blockFeelings(boolean inState)
	{
		this.m_canFeel = inState;
	}
	
	/**
	 * Gets the entity who's mind this is
	 * 
	 * @return entity
	 */
	public RemoteEntity getEntity()
	{
		return this.m_entity;
	}
	
	/**
	 * Gets the behavior having this name
	 * 
	 * @param inName	name
	 * @return			behavior
	 */
	public Behavior getBehaviour(String inName)
	{
		return this.m_behaviours.get(inName);
	}
	
	/**
	 * Gets all the behaviors the entity has
	 * 
	 * @return behaviors
	 */
	public Collection<Behavior> getBehaviours()
	{
		return this.m_behaviours.values();
	}

	/**
	 * Removes all behaviors from the entity 
	 */
	public void clearBehaviours()
	{
		this.m_behaviours.clear();
	}
	
	/**
	 * Gets all movement desires of the entity
	 * 
	 * @return movement desires
	 */
	public List<Desire> getMovementDesires()
	{
		return this.m_movementNavigation.getDesires();
	}
	
	/**
	 * Gets the currently highest priority from the movement desires
	 * 
	 * @return priority
	 */
	public int getHighestMovementPriority()
	{
		return this.m_movementNavigation.getHighestPriority();
	}
	
	/**
	 * Gets the currently highest priority from the movement desires
	 * 
	 * @return priorities
	 */
	public int getHighestActionPriority()
	{
		return this.m_targetNavigation.getHighestPriority();
	}
	
	/**
	 * Gets all action desires of the entity
	 * 
	 * @return action desires
	 */
	public List<Desire> getActionDesires()
	{
		return this.m_targetNavigation.getDesires();
	}
	
	/**
	 * Gets the movement desire with given type
	 * 
	 * @param inClass	type
	 * @return			desire
	 */
	public<T extends Desire> T getMovementDesire(Class<T> inClass)
	{
		for(Desire desire : this.getMovementDesires())
		{
			if(desire.getClass().equals(inClass) || desire.getClass().getSuperclass().equals(inClass))
				return inClass.cast(desire);
		}
		return null;
	}
	
	/**
	 * Gets the action desire with given type
	 * 
	 * @param inClass	type
	 * @return			desire
	 */
	public<T extends Desire> T getActionDesire(Class<T> inClass)
	{
		for(Desire desire : this.getActionDesires())
		{
			if(desire.getClass().equals(inClass) || desire.getClass().getSuperclass().equals(inClass))
				return inClass.cast(desire);
		}
		return null;
	}
	
	/**
	 * Adds a movement desire with given priority
	 * 
	 * @param inDesire		desire
	 * @param inPriority	priority
	 */
	public void addMovementDesire(Desire inDesire, int inPriority)
	{
		this.m_movementNavigation.addDesire(inDesire, inPriority);
	}
	
	/**
	 * Adds a action desire with given priority
	 * 
	 * @param inDesire		desire
	 * @param inPriority	priority
	 */
	public void addActionDesire(Desire inDesire, int inPriority)
	{
		this.m_targetNavigation.addDesire(inDesire, inPriority);
	}
	
	/**
	 * Removes all movement desires 
	 */
	public void clearMovementDesires()
	{
		this.m_movementNavigation.clearDesires();
	}
	
	/**
	 * Removes all action desires 
	 */
	public void clearActionDesires()
	{
		this.m_targetNavigation.clearDesires();
	}
	
	/**
	 * Removes the movement desire with given type and the lowest priority of his type
	 * 
	 * @param inToRemove	type
	 * @return				true if it got removed, false if not
	 */
	public boolean removeMovementDesire(Class<? extends Desire> inToRemove)
	{
		return this.m_targetNavigation.removeDesireByType(inToRemove);
	}
	
	/**
	 * Removes all movement desires with given type
	 * 
	 * @param inToRemove type
	 */
	public void removeMovementDesires(Class<? extends Desire> inToRemove)
	{
		while(this.removeMovementDesire(inToRemove)){}
	}
	
	/**
	 * Removes the action desire with given type and the lowest priority of his type
	 * 
	 * @param inToRemove	type
	 * @return				true if it got removed, false if not
	 */
	public boolean removeActionDesire(Class<? extends Desire> inToRemove)
	{
		return this.m_movementNavigation.removeDesireByType(inToRemove);
	}
	
	/**
	 * Removes all action desires with given type
	 * 
	 * @param inToRemove type
	 */
	public void removeActionDesires(Class<? extends Desire> inToRemove)
	{
		while(this.removeActionDesire(inToRemove)){}
	}
	
	/**
	 * Fixes yaw rotation
	 * 
	 * @param yaw	yaw of the entity
	 */
	public void fixYaw(float yaw)
	{
		this.yaw = yaw;
		this.changedYaw = true;
	}
	
	/**
	 * Fixes pitch rotation
	 * 
	 * @param pitch	pitch of the entity
	 */
	public void fixPitch(float pitch)
	{
		this.pitch = pitch;
		this.changedPitch = true;
	}
	
	/**
	 * Updates the mind
	 */
	public void tick()
	{
		if(this.m_entity.getHandle() == null)
			return;
		
		if(this.changedYaw)
		{
			this.m_entity.getHandle().yaw = yaw;
			this.m_entity.getHandle().az = yaw;
		}
		
		if(this.changedPitch)
		{
			this.m_entity.getHandle().pitch = pitch;
		}
		
		if(this.canFeel())
		{
			for(Behavior behaviour : this.m_behaviours.values())
			{
				behaviour.run();
			}
		}
		this.m_movementNavigation.onUpdate();
		this.m_targetNavigation.onUpdate();
	}
}