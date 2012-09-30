package de.kumpelblase2.removeentities.thinking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public class Mind
{
	private Map<Integer, List<Behaviour>> m_behaviours;
	private RemoteEntity m_entity;
	private boolean m_canFeel = true;
	
	public Mind(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	public void addBehaviour(Behaviour inBehaviour, Integer inPriority)
	{
		if(this.m_behaviours.containsKey(inPriority))
		{
			this.m_behaviours.get(inPriority).add(inBehaviour);
		}
		else
		{
			List<Behaviour> behaviours = new ArrayList<Behaviour>();
			behaviours.add(inBehaviour);
			this.m_behaviours.put(inPriority, behaviours);
		}
	}
	
	public boolean removeBehaviour(String inName)
	{
		boolean found = false;
		Iterator<Entry<Integer, List<Behaviour>>> iterator = this.m_behaviours.entrySet().iterator();
		while(iterator.hasNext())
		{
			Entry<Integer, List<Behaviour>> entry = iterator.next();
			Iterator<Behaviour> it = entry.getValue().iterator();
			while(it.hasNext())
			{
				Behaviour b = it.next();
				if(b.getName().equals(inName))
				{
					it.remove();
					found = true;
				}
			}
			if(entry.getValue().size() == 0)
				iterator.remove();
		}
		return found;
	}
	
	public boolean hasBehaviour(String inName)
	{
		for(List<Behaviour> behaviours : this.m_behaviours.values())
		{
			for(Behaviour b : behaviours)
			{
				if(b.getName().equals(inName))
					return true;
			}
		}
		return false;
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
}