package de.kumpelblase2.remoteentities.api.thinking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import de.kumpelblase2.remoteentities.api.events.RemoteDesireAddEvent;

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
		RemoteDesireAddEvent event = new RemoteDesireAddEvent(inDesire.getRemoteEntity(), inDesire, inPriority);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		this.m_desires.add(new DesireItem(event.getDesire(), event.getPriority()));
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
			else if(!item.getDesire().isContinuous() && this.m_executingDesires.contains(item))			 
				return false;
		} 
		return true;
	}
	 
	public static boolean areTasksCompatible(Desire inFirstDesire, Desire inSecondDesire)
	{
		return inFirstDesire.getType() != inSecondDesire.getType();
	}
	
	public List<DesireItem> getDesires()
	{
		return this.m_desires;
	}
	
	public boolean removeDesireByType(Class<? extends Desire> inType)
	{		
		List<DesireItem> temp = new ArrayList<DesireItem>();
		for(DesireItem item : this.m_desires)
		{
			if(item.getDesire().getClass().equals(inType) || item.getDesire().getClass().getSuperclass().equals(inType))
				temp.add(item);
		}
		
		if(temp.size() == 0)
			return false;
		
		if(temp.size() > 1)
		{
			DesireItem lowest = temp.get(0);
			for(DesireItem item : temp)
			{
				if(this.hasLowestPriority(item))
				{
					lowest = item;
					break;
				}
			}
			temp.clear();
			temp.add(lowest);
			lowest.getDesire().stopExecuting();
		}
		else
			temp.get(0).getDesire().stopExecuting();

		this.m_desires.remove(temp.get(0));
		this.m_executingDesires.remove(temp.get(0));
		return true;
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
	
	protected boolean hasLowestPriority(DesireItem inItem)
	{
		int lowest = inItem.getPriority();
		for(DesireItem item : this.m_desires)
		{
			if(item.getDesire().getClass().equals(inItem.getDesire().getClass()) || item.getDesire().getClass().getSuperclass().equals(inItem.getDesire().getClass()))
			{
				if(item.getPriority() < lowest)
					lowest = item.getPriority();
			}
		}
		return lowest == inItem.getPriority();
	}
}
