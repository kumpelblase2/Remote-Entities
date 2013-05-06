package de.kumpelblase2.remoteentities;

import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.features.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.exceptions.*;
import org.bukkit.*;
import java.util.*;

public class CreateEntityContext
{
	private RemoteEntityType m_type;
	private String m_name;
	private int m_id;
	private Location m_location;
	private List<Feature> m_features;
	private List<Behavior> m_behaviors;
	private final EntityManager m_manager;
	private boolean m_stationary = false;
	private boolean m_pushable = true;
	private float m_speed = -1;
	private int m_maxHealth = -1;
	private final List<DesireItem> m_movementDesires;
	private final List<DesireItem> m_actionDesires;
	
	CreateEntityContext(EntityManager inManager)
	{
		this.m_features = new ArrayList<Feature>();
		this.m_behaviors = new ArrayList<Behavior>();
		this.m_manager = inManager;
		this.m_id = inManager.getNextFreeID();
		this.m_actionDesires = new ArrayList<DesireItem>();
		this.m_movementDesires = new ArrayList<DesireItem>();
	}
	
	/**
	 * Sets the type of the entity
	 * 
	 * @param inType	Type
	 * @return			Context
	 */
	public CreateEntityContext withType(RemoteEntityType inType)
	{
		this.m_type = inType;
		return this;
	}
	
	/**
	 * Sets the location to spawn at. When the location is null, it won't get spawned.
	 * 
	 * @param inLocation	Location to spawn at
	 * @return				Context
	 */
	public CreateEntityContext atLocation(Location inLocation)
	{
		this.m_location = inLocation;
		return this;
	}
	
	/**
	 * Sets the id of the entity. When the ID is already used, the next free ID will be used.
	 * That means that you might not get the wanted entity id
	 * 
	 * @param inID	Id of the entity
	 * @return		Context
	 */
	public CreateEntityContext withID(int inID)
	{
		this.m_id = inID;
		return this;
	}
	
	/**
	 * Sets the name of the entity
	 * 
	 * @param inName	Name
	 * @return			Context
	 */
	public CreateEntityContext withName(String inName)
	{
		this.m_name = inName;
		return this;
	}
	
	/**
	 * Features that the entity should have on creation
	 * 
	 * @param inFeatures	Features
	 * @return				Context
	 */
	public CreateEntityContext withFeatures(Feature... inFeatures)
	{
		this.m_features = new ArrayList<Feature>(Arrays.asList(inFeatures));
		return this;
	}
	
	/**
	 * Behaviors that the entity should have on creation
	 * 
	 * @param inBehaviors	Behaviors
	 * @return				Context
	 */
	public CreateEntityContext withBehaviors(Behavior... inBehaviors)
	{
		this.m_behaviors = new ArrayList<Behavior>(Arrays.asList(inBehaviors));
		return this;
	}
	
	/**
	 * Sets the stationary status on creation
	 * 
	 * @param inStationary	Stationary
	 * @return				Context
	 */
	public CreateEntityContext asStationary(boolean inStationary)
	{
		this.m_stationary = inStationary;
		return this;
	}
	
	/**
	 * Sets the pushable status on creation
	 * 
	 * @param inPushable	Pushable
	 * @return				Context
	 */
	public CreateEntityContext asPushable(boolean inPushable)
	{
		this.m_pushable = inPushable;
		return this;
	}
	
	/**
	 * Sets the speed on creation
	 * 
	 * @param inSpeed	Speed
	 * @return			Context
	 */
	public CreateEntityContext withSpeed(float inSpeed)
	{
		this.m_speed  = inSpeed;
		return this;
	}
	
	/**
	 * Sets the max health on creation
	 * 
	 * @param inMaxHealth	Max Health
	 * @return				Context
	 */
	public CreateEntityContext withMaxHealth(int inMaxHealth)
	{
		this.m_maxHealth = inMaxHealth;
		return this;
	}
	
	/**
	 * Sets the initial movement desires
	 * 
	 * @param inDesires	movement desires
	 * @return			Context
	 */
	public CreateEntityContext withMovementDesires(DesireItem... inDesires)
	{
		this.m_movementDesires.clear();
		this.m_movementDesires.addAll(Arrays.asList(inDesires));
		return this;
	}
	
	/**
	 * Sets the initial action desires
	 * 
	 * @param inDesires	action desires
	 * @return			Context
	 */
	public CreateEntityContext withActionDesires(DesireItem... inDesires)
	{
		this.m_actionDesires.clear();
		this.m_actionDesires.addAll(Arrays.asList(inDesires));
		return this;
	}
	
	/**
	 * Creates the entity with the earlier specified parameters
	 * 
	 * @return					Created entity
	 * @throws NoTypeException	When no type is specified
	 * @throws NoNameException	When no name is specified while trying to spawn a named entity
	 */
	public RemoteEntity create()
	{
		RemoteEntity created;
		
		if(this.m_type == null)
			throw new NoTypeException();
		
		this.m_id = this.m_manager.getNextFreeID(this.m_id);
		
		if(this.m_type.isNamed())
		{
			if(this.m_name == null)
				throw new NoNameException("Tried to spawn a named entity without name");
			
			created = this.m_manager.createNamedEntity(this.m_type, this.m_id, this.m_name);
		}
		else
			created = this.m_manager.createEntity(this.m_type, this.m_id);
		
		for(Feature feature : this.m_features)
		{
			created.getFeatures().addFeature(feature);
		}
		
		for(Behavior behavior : this.m_behaviors)
		{
			created.getMind().addBehaviour(behavior);
		}
		
		for(DesireItem desire : this.m_movementDesires)
		{
			created.getMind().addMovementDesire(desire.getDesire(), desire.getPriority());
		}
		
		for(DesireItem desire : this.m_actionDesires)
		{
			created.getMind().addTargetingDesire(desire.getDesire(), desire.getPriority());
		}
		
		created.setStationary(this.m_stationary);
		created.setPushable(this.m_pushable);
		if(this.m_speed != -1)
			created.setSpeed(this.m_speed);
		
		if(this.m_location != null)
			created.spawn(this.m_location);
		
		if(this.m_maxHealth != -1 && created.getBukkitEntity() != null)
			created.getBukkitEntity().setMaxHealth(this.m_maxHealth);
		
		return created;
	}
}
