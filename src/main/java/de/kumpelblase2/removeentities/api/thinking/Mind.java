package de.kumpelblase2.removeentities.api.thinking;

import java.util.*;
import net.minecraft.server.PathfinderGoal;
import de.kumpelblase2.removeentities.api.RemoteEntity;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.utilities.ReflectionUtil;

public class Mind
{
	private Map<String, Behaviour> m_behaviours;
	private RemoteEntity m_entity;
	private boolean m_canFeel = true;
	
	public Mind(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_behaviours = new HashMap<String, Behaviour>();
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
		try
		{
			List<Desire> desires = new ArrayList<Desire>();
			for(Object o : ((RemoteEntityHandle)this.m_entity.getHandle()).getGoalSelector().getGoals())
			{
				desires.add((Desire)ReflectionUtil.getGoalFromItem(o));
			}
			return desires;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Desire> getActionDesires()
	{
		try
		{
			List<Desire> desires = new ArrayList<Desire>();
			for(Object o : ((RemoteEntityHandle)this.m_entity.getHandle()).getTargetSelector().getGoals())
			{
				desires.add((Desire)ReflectionUtil.getGoalFromItem(o));
			}
			return desires;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
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
		((RemoteEntityHandle)this.m_entity.getHandle()).getGoalSelector().addGoal((PathfinderGoal)inDesire, inPriority);
	}
	
	public void addTargetDesire(Desire inDesire, int inPriority)
	{
		((RemoteEntityHandle)this.m_entity.getHandle()).getTargetSelector().addGoal((PathfinderGoal)inDesire, inPriority);
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
	}
}