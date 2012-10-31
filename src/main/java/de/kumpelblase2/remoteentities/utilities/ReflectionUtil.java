package de.kumpelblase2.remoteentities.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.*;

public final class ReflectionUtil
{
	public static void replaceGoalSelector(EntityLiving inEntity, String inSelectorName, PathfinderGoalSelector inNewSelector)
	{
		try
		{
			Field goalSelectorField = inEntity.getClass().getDeclaredField(inSelectorName);
			goalSelectorField.setAccessible(true);
			goalSelectorField.set(inEntity, inNewSelector);
		}
		catch(Exception e){}
	}
	
	public static PathfinderGoal getGoalFromItem(Object o)
	{
		try
		{
			Field goalField = o.getClass().getDeclaredField("a");
			goalField.setAccessible(true);
			return (PathfinderGoal)goalField.get(o);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void registerEntityType(Class<?> inClass, String name, int inID)
	{
		try
		{
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;
 
            Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);
 
            a.invoke(a, inClass, name, inID);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
	}
	
	public static float getSpeed(EntityLiving inEntity)
	{
		try
		{
			Field speed = inEntity.getClass().getDeclaredField("bw");
			return speed.getFloat(inEntity);
		}
		catch(Exception e)
		{
			return 0F;
		}
	}
	
	public static float getSpeedModifier(EntityLiving inEntity)
	{
		try
		{
			Field speed = inEntity.getClass().getDeclaredField("bB");
			return speed.getFloat(inEntity);
		}
		catch(Exception e)
		{
			return 0F;
		}
	}
}
