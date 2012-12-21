package de.kumpelblase2.remoteentities.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.PathfinderGoalSelector;

public final class ReflectionUtil
{
	private static Set<Class<?>> m_registeredClasses = new HashSet<Class<?>>();
	
	/**
	 * Replaces the goal selector of an entity with a new one
	 * 
	 * @param inEntity			entity
	 * @param inSelectorName	name of the selector (targetSelector or movementSelector)
	 * @param inNewSelector		new selector
	 */
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
	
	/**
	 * Registers custom entity class at the native minecraft entity enum
	 * 
	 * @param inClass	class of the entity
	 * @param name		minecraft entity name
	 * @param inID		minecraft entity id
	 */
	public static void registerEntityType(Class<?> inClass, String name, int inID)
	{
		if(m_registeredClasses.contains(inClass))
			return;
		
		try
		{
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;
 
            Method a = net.minecraft.server.v1_4_6.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);
 
            a.invoke(a, inClass, name, inID);
            m_registeredClasses.add(inClass);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
	}
	
	/**
	 * Gets the speed of an entity
	 * 
	 * @param inEntity	entity
	 * @return			speed
	 * @Deprecated 		field is wrong
	 */
	@Deprecated
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
	
	/**
	 * Gets the speed modifier of an entity
	 * 
	 * @param inEntity	entity
	 * @return			modifier
	 * @Deprecated		field is wrong
	 */
	@Deprecated
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
