package de.kumpelblase2.remoteentities.api.thinking;

import java.util.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class Mind
{
	private final Set<Behavior> m_behaviours;
	private final RemoteEntity m_entity;
	private boolean m_canFeel = true;
	private DesireSelector m_targetNavigation;
	private DesireSelector m_movementNavigation;
	private boolean m_hasFixedYaw = false;
	private boolean m_hasFixedPitch = false;
	private boolean m_hasFixedHeadYaw = false;
	private float m_fixedYaw;
	private float m_fixedPitch;
	private float m_fixedHeadYaw;

	public Mind(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_behaviours = new HashSet<Behavior>();
		this.m_targetNavigation = new DesireSelector(inEntity);
		this.m_movementNavigation = new DesireSelector(inEntity);
	}

	/**
	 * Adds a behavior to the entity
	 *
	 * @param inBehaviour behavior
	 */
	public void addBehaviour(Behavior inBehaviour)
	{
		inBehaviour.onAdd();
		this.m_behaviours.add(inBehaviour);
	}

	/**
	 * Removes a behavior from the entity with given name
	 *
	 * @param inName	name
	 * @return			true if behavior got removed, false if not
	 * @deprecated in favor of #removeBehavior(Class)
	 */
	@Deprecated
	public boolean removeBehaviour(String inName)
	{
		Iterator<Behavior> iterator = this.m_behaviours.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().getName().equals(inName))
			{
				iterator.remove();
				return true;
			}
		}

		return false;
	}

	/**
	 * Removes a given type of behavior from the entity.
	 *
	 * @param inType    The type of behavior to remove.
	 * @return          True or false, depending on whether or not the type of behavior could be removed or not
	 */
	public boolean removeBehavior(Class<? extends Behavior> inType)
	{
		Iterator<Behavior> iterator = this.m_behaviours.iterator();
		while(iterator.hasNext())
		{
			if(inType.isAssignableFrom(iterator.next().getClass()))
			{
				iterator.remove();
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the entity has a specific behavior
	 *
	 * @param inName 	name
	 * @return			true if the entity has such behavior, false if not
	 * @deprecated in favor of #hasBehavior(Class)
	 */
	@Deprecated
	public boolean hasBehaviour(String inName)
	{
		for(Behavior behavior : this.m_behaviours)
		{
			if(behavior.getName().equals(inName))
				return true;
		}

		return false;
	}

	/**
	 * Checks if the entity has a specific type of behavior.
	 *
	 * @param inType    The type of behavior to to check for.
	 * @return          True if the entity has such behavior, false if not
	 */
	public boolean hasBehavior(Class<? extends Behavior> inType)
	{
		for(Behavior behavior : this.m_behaviours)
		{
			if(inType.isAssignableFrom(behavior.getClass()))
				return true;
		}

		return false;
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
	 * @deprecated in favor of #getBehavior(Class)
	 */
	@Deprecated
	public Behavior getBehaviour(String inName)
	{
		for(Behavior behavior : this.m_behaviours)
		{
			if(behavior.getName().equals(inName))
				return behavior;
		}

		return null;
	}

	/**
	 * Gets the behavior of the given type.
	 *
	 * @param inType    The type of the behavior
	 * @return          The behavior
	 */
	public <T extends Behavior> T getBehavior(Class<T> inType)
	{
		for(Behavior behavior : this.m_behaviours)
		{
			if(inType.isAssignableFrom(behavior.getClass()))
				return (T)behavior;
		}

		return null;
	}

	/**
	 * Gets all the behaviors the entity has
	 *
	 * @return behaviors
	 */
	public Collection<Behavior> getBehaviours()
	{
		return this.m_behaviours;
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
	public List<DesireItem> getMovementDesires()
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
	 * Gets the currently highest priority from the targeting desires
	 *
	 * @return priorities
	 */
	public int getHighestTargetingPriority()
	{
		return this.m_targetNavigation.getHighestPriority();
	}

	/**
	 * Gets all action desires of the entity
	 *
	 * @return action desires
	 */
	public List<DesireItem> getTargetingDesires()
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
		for(DesireItem desire : this.getMovementDesires())
		{
			if(desire.getDesire().getClass().equals(inClass) || desire.getDesire().getClass().getSuperclass().equals(inClass))
				return inClass.cast(desire.getDesire());
		}
		return null;
	}

	/**
	 * Gets the action desire with given type
	 *
	 * @param inClass	type
	 * @return			desire
	 */
	public<T extends Desire> T getTargetingDesire(Class<T> inClass)
	{
		for(DesireItem desire : this.getTargetingDesires())
		{
			if(desire.getDesire().getClass().equals(inClass) || desire.getDesire().getClass().getSuperclass().equals(inClass))
				return inClass.cast(desire.getDesire());
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
	 * Adds all given desires to the movement desires with given priority.
	 *
	 * @param inDesires		Array of DesireItems containing the desire and it's priority
	 * @see #addMovementDesire(Desire, int)
	 */
	public void addMovementDesires(DesireItem... inDesires)
	{
		for(DesireItem item : inDesires)
		{
			this.addMovementDesire(item.getDesire(), item.getPriority());
		}
	}

	/**
	 * Adds a targeting desire with given priority
	 *
	 * @param inDesire		desire
	 * @param inPriority	priority
	 */
	public void addTargetingDesire(Desire inDesire, int inPriority)
	{
		this.m_targetNavigation.addDesire(inDesire, inPriority);
	}

	/**
	 * Adds all given desires to the targeting desires with given priority.
	 *
	 * @param inDesires		Array of DesireItems containing the desire and it's priority
	 * @see #addTargetingDesire(Desire, int)
	 */
	public void addTargetingDesires(DesireItem... inDesires)
	{
		for(DesireItem item : inDesires)
		{
			this.addTargetingDesire(item.getDesire(), item.getPriority());
		}
	}

	/**
	 * Removes all movement desires
	 */
	public void clearMovementDesires()
	{
		this.m_movementNavigation.clearDesires();
	}

	/**
	 * Removes all targeting desires
	 */
	public void clearTargetingDesires()
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
		return this.m_movementNavigation.removeDesireByType(inToRemove);
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
	 * Removes the targeting desire with given type and the lowest priority of his type
	 *
	 * @param inToRemove	type
	 * @return				true if it got removed, false if not
	 */
	public boolean removeTargetingDesire(Class<? extends Desire> inToRemove)
	{
		return this.m_targetNavigation.removeDesireByType(inToRemove);
	}

	/**
	 * Removes all targeting desires with given type
	 *
	 * @param inToRemove type
	 */
	public void removeTargetingDesires(Class<? extends Desire> inToRemove)
	{
		while(this.removeTargetingDesire(inToRemove)){}
	}

	/**
	 * Fixes the yaw of an entity to a specific value.
	 *
	 * @param inYaw	Fixed yaw value
	 */
	public void fixYawAt(float inYaw)
	{
		this.m_hasFixedYaw = true;
		this.m_fixedYaw = inYaw;
	}

	/**
	 * Removes the fix of the yaw value
	 */
	public void resetFixedYaw()
	{
		this.m_hasFixedYaw = false;
	}

	/**
	 * Fixes the pitch of an entity to a specific value.
	 *
	 * @param inPitch	Fixed yaw value
	 */
	public void fixPitchAt(float inPitch)
	{
		this.m_hasFixedPitch = true;
		this.m_fixedPitch = inPitch;
	}

	/**
	 * Removes the fix of the pitch value
	 */
	public void resetFixedPitch()
	{
		this.m_hasFixedPitch = false;
	}

	/**
	 * Fixes the yaw of an entity to a specific value.
	 *
	 * @param inHeadYaw	Fixed yaw value
	 */
	public void fixHeadYawAt(float inHeadYaw)
	{
		this.m_hasFixedHeadYaw = true;
		this.m_fixedHeadYaw = inHeadYaw;
	}

	/**
	 * Removes the fix of the yaw value
	 */
	public void resetFixedHeadYaw()
	{
		this.m_hasFixedHeadYaw = false;
	}

	/**
	 * Updates the mind
	 */
	public void tick()
	{
		if(this.m_entity.getHandle() == null)
			return;

		if(this.m_hasFixedYaw)
			this.m_entity.setYaw(this.m_fixedYaw);

		if(this.m_hasFixedPitch)
			this.m_entity.setPitch(this.m_fixedPitch);

		if(this.m_hasFixedHeadYaw)
			this.m_entity.setHeadYaw(this.m_fixedHeadYaw);


		if(this.canFeel())
		{
			for(Behavior behaviour : this.m_behaviours)
			{
				behaviour.run();
			}
		}

		this.m_movementNavigation.onUpdate();
		this.m_targetNavigation.onUpdate();
	}
}