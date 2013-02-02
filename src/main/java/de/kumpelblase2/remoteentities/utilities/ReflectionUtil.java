package de.kumpelblase2.remoteentities.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import net.minecraft.server.v1_4_R1.*;

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
		catch(Exception e)
		{
		}
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
 
            Method a = net.minecraft.server.v1_4_R1.EntityTypes.class.getDeclaredMethod("a", args);
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
	 */
	public static float getSpeed(EntityLiving inEntity)
	{
		try
		{
			Field speed = inEntity.getClass().getDeclaredField("bH");
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
	 */
	public static float getSpeedModifier(EntityLiving inEntity)
	{
		try
		{
			Field speed = inEntity.getClass().getDeclaredField("bP");
			return speed.getFloat(inEntity);
		}
		catch(Exception e)
		{
			return 0F;
		}
	}
	
	public static List<ParameterData> getParameterDataForClass(Object inClass)
	{
		Class<?> clazz = inClass.getClass();
		System.out.println(clazz.toString());
		List<ParameterData> parameters = new ArrayList<ParameterData>();
		Set<String> membersLooked = new HashSet<String>();
		while(clazz != Object.class && clazz != Desire.class)
		{
			for(Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
				System.out.println("current field: " + field.getName() + "_:_" + field.isAnnotationPresent(SerializeAs.class));
				if(membersLooked.contains(field.getName()))
					continue;
				
				membersLooked.add(field.getName());
				for(Annotation an : field.getAnnotations())
				{
					if(an instanceof SerializeAs)
					{
						SerializeAs sas = (SerializeAs)an;
						try
						{
							System.out.println("Added field: " + field.getName());
							Object value = field.get(inClass);
							parameters.add(new ParameterData(sas.pos(), field.getType().getName(), value, sas.special()));
						}
						catch(Exception e)
						{
							RemoteEntities.getInstance().getLogger().warning("Unable to add desire parameter. " + e.getMessage());
						}
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		return parameters;
	}
}
