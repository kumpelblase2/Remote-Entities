package de.kumpelblase2.remoteentities.api.thinking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Navigation
{
	private List<DesireItem> m_desires;
	private List<DesireItem> m_executingDesires;
	private int m_delay = 0;
	
	public Navigation()
	{
		this.m_desires = new ArrayList<DesireItem>();
		this.m_executingDesires = new ArrayList<DesireItem>();
	}
	
	public void onUpdate()
	{
		if(++this.m_delay % 3 == 0)
		{
			for(DesireItem item : this.m_desires)
			{
				if(this.m_executingDesires.contains(item))
				{
					if(this.hasHighestPriority(item) && item.getDesire().canContinue())
						continue;
					
					item.getDesire().stopExecuting();
					this.m_executingDesires.remove(item);
				}
							 
				if(this.hasHighestPriority(item) && item.getDesire().shouldExecute())
				{
					item.getDesire().startExecuting();
					this.m_executingDesires.add(item);
				}
			}
			this.m_delay = 0;
		}
		else
		{
			Iterator<DesireItem> it = this.m_executingDesires.iterator();
			while(it.hasNext())
			{
				DesireItem item = it.next();
				if(!item.getDesire().canContinue())
				{
					item.getDesire().stopExecuting();
					it.remove();
				}
			}
		}
		
		Iterator<DesireItem> it = this.m_executingDesires.iterator();
		while(it.hasNext())
		{
			DesireItem item = it.next();
			if(!item.getDesire().update())
				it.remove();
		}
	}
	 
	public void addDesire(Desire inDesire, int inPriority)
	{
		this.m_desires.add(new DesireItem(inDesire, inPriority));
	}
	 
	public boolean hasHighestPriority(DesireItem inItem)
	{
		for(DesireItem item : this.m_desires)
		{
			if(item.equals(inItem))
				continue;
			
			if(inItem.getPriority() >= item.getPriority())
			{
				if(!areTasksCompatible(item.getDesire(), inItem.getDesire()) && this.m_executingDesires.contains(item))
					return false; 
			}
			else if(!item.getDesire().isContinous() && this.m_executingDesires.contains(item))			 
				return false;
		} 
		return true;
	}
	 
	public static boolean areTasksCompatible(Desire inFirstDesire, Desire inSecondDesire)
	{
		return inFirstDesire.getType() != inSecondDesire.getType();
	}
	
	public List<Desire> getDesires()
	{
		List<Desire> tempDesires = new ArrayList<Desire>();
		for(DesireItem item : this.m_desires)
			tempDesires.add(item.getDesire());
		
		return tempDesires;
	}
	
	public boolean removeDesireByType(Class<? extends Desire> inType)
	{
		Iterator<DesireItem> it = this.m_desires.iterator();
		boolean found = false;
		while(it.hasNext())
		{
			DesireItem item = it.next();
			if(item.getDesire().getClass().equals(inType) || item.getDesire().getClass().getSuperclass().equals(inType))
			{
				it.remove();
				found = true;
			}
		}
		
		it = this.m_executingDesires.iterator();
		while(it.hasNext())
		{
			DesireItem item = it.next();
			if(item.getDesire().getClass().equals(inType))
			{
				item.getDesire().stopExecuting();
				it.remove();
				found = true;
			}
		}
		return found;
	}
	
	public void clearDesires()
	{
		for(DesireItem item : this.m_executingDesires)
		{
			item.getDesire().stopExecuting();
		}
		this.m_desires.clear();
		this.m_executingDesires.clear();
	}
	
	public int getHighestPriority()
	{
		int highest = 0;
		for(DesireItem item : this.m_desires)
		{
			if(item.getPriority() > highest)
				highest = item.getPriority();
		}
		
		return highest;
	}
}
