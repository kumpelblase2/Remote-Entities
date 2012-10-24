package de.kumpelblase2.removeentities.api.thinking;

import java.util.*;
import de.kumpelblase2.removeentities.api.RemoteEntity;

public class Mind
{
	private Map<String, Behaviour> m_behaviours;
	private RemoteEntity m_entity;
	private boolean m_canFeel = true;
	private Navigation m_targetNavigation;
	private Navigation m_movementNavigation;
	
	public Mind(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_behaviours = new HashMap<String, Behaviour>();
		this.m_targetNavigation = new Navigation();
		this.m_movementNavigation = new Navigation();
	}
	
	public void addBehaviour(Behaviour inBehaviour)
	{
		this.m_behaviours.put(inBehaviour.getName(), inBehaviour);
	}
	
	public boolean removeBehaviour(String inName)
	{
		return this.m_behaviours.remove(inName) != null;
	}
	
	public boolean hasBehaviour(String inName)
	{
		return this.m_behaviours.containsKey(inName);
	}
	
	public boolean canFeel()
	{
		return this.m_canFeel;
	}
	
	public void blockFeelings(boolean inState)
	{
		this.m_canFeel = inState;
	}
	
	public RemoteEntity getEntity()
	{
		return this.m_entity;
	}
	
	public Behaviour getBehaviour(String inName)
	{
		return this.m_behaviours.get(inName);
	}
	
	public Collection<Behaviour> getBehaviours()
	{
		return this.m_behaviours.values();
	}

	public void clearBehaviours()
	{
		this.m_behaviours.clear();
	}
	
	public List<Desire> getMovementDesires()
	{
		return this.m_movementNavigation.getDesires();
	}
	
	public List<Desire> getActionDesires()
	{
		return this.m_targetNavigation.getDesires();
	}
	
	public<T extends Desire> T getMovementDesire(Class<T> inClass)
	{
		for(Desire desire : this.getMovementDesires())
		{
			if(desire.getClass().equals(inClass))
				return inClass.cast(desire);
		}
		return null;
	}
	
	public void addMovementDesire(Desire inDesire, int inPriority)
	{
		//((RemoteEntityHandle)this.m_entity.getHandle()).getGoalSelector().addGoal((PathfinderGoal)inDesire, inPriority);
		this.m_movementNavigation.addDesire(inDesire, inPriority);
	}
	
	public void addTargetDesire(Desire inDesire, int inPriority)
	{
		//((RemoteEntityHandle)this.m_entity.getHandle()).getTargetSelector().addGoal((PathfinderGoal)inDesire, inPriority);
		this.m_targetNavigation.addDesire(inDesire, inPriority);
	}
	
	public void clearMovementDesires()
	{
		//((RemoteEntityHandle)this.m_entity.getHandle()).getGoalSelector().clearGoals();
		this.m_movementNavigation.clearDesires();
	}
	
	public void clearTargetDesires()
	{
		//((RemoteEntityHandle)this.m_entity.getHandle()).getTargetSelector().clearGoals();
		this.m_targetNavigation.clearDesires();
	}
	
	public void tick()
	{
		if(this.canFeel())
		{
			for(Behaviour behaviour : this.m_behaviours.values())
			{
				behaviour.run();
			}
		}
		this.m_movementNavigation.onUpdate();
		this.m_targetNavigation.onUpdate();
	}
}