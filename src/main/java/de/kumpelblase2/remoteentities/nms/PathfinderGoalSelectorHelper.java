package de.kumpelblase2.remoteentities.nms;

import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.server.v1_6_R1.PathfinderGoal;
import net.minecraft.server.v1_6_R1.PathfinderGoalSelector;
import de.kumpelblase2.remoteentities.api.thinking.Desire;

public class PathfinderGoalSelectorHelper
{
	private final PathfinderGoalSelector m_selector;

	public PathfinderGoalSelectorHelper(PathfinderGoalSelector inSelector)
	{
		this.m_selector = inSelector;
	}

	public Object getGoal(Class<? extends Desire> inDesire)
	{
		try
		{
			for(Object item : this.getGoals())
			{
				if(item.getClass().getField("a").get(item).getClass().isAssignableFrom(inDesire))
					return item;
			}
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public List getGoals()
	{
		try
		{
			Field arrayListField = PathfinderGoalSelector.class.getDeclaredField("a");
			arrayListField.setAccessible(true);
			return (List)arrayListField.get(this.m_selector);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void clearGoals()
	{
		this.getGoals().clear();
		try
		{
			Field arrayListField = PathfinderGoalSelector.class.getDeclaredField("b");
			arrayListField.setAccessible(true);
			((List)arrayListField.get(this.m_selector)).clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void addGoal(PathfinderGoal inGoal, int inPriority)
	{
		this.m_selector.a(inPriority, inGoal);
	}
}