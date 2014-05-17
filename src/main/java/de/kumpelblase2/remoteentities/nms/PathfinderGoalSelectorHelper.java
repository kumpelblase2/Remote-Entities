package de.kumpelblase2.remoteentities.nms;

import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.server.v1_7_R3.PathfinderGoal;
import net.minecraft.server.v1_7_R3.PathfinderGoalSelector;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class PathfinderGoalSelectorHelper
{
	private final PathfinderGoalSelector m_selector;

	public PathfinderGoalSelectorHelper(PathfinderGoalSelector inSelector)
	{
		this.m_selector = inSelector;
	}

	@SuppressWarnings("rawtypes")
	public List getGoals()
	{
		try
		{
			Field arrayListField = ReflectionUtil.getOrRegisterField(PathfinderGoalSelector.class, "b");
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
			Field arrayListField = ReflectionUtil.getOrRegisterField(PathfinderGoalSelector.class, "b");
			((List)arrayListField.get(this.m_selector)).clear();
			arrayListField = ReflectionUtil.getOrRegisterField(PathfinderGoalSelector.class, "c");
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