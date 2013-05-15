package de.kumpelblase2.remoteentities.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.PathfinderGoalSelector;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;

public final class ReflectionUtil
{
	private static final Map<String, Field> s_cachedFields = new HashMap<String, Field>();

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
			if(s_cachedFields.containsKey(inSelectorName))
			{
				Field f = s_cachedFields.get(inSelectorName);
				f.set(inEntity, inNewSelector);
			}
			else
			{
				Field goalSelectorField = inEntity.getClass().getDeclaredField(inSelectorName);
				goalSelectorField.setAccessible(true);
				goalSelectorField.set(inEntity, inNewSelector);
				s_cachedFields.put(inSelectorName, goalSelectorField);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
		try
		{
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;

            Method a = net.minecraft.server.v1_5_R3.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);

            a.invoke(a, inClass, name, inID);
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
			if(s_cachedFields.containsKey("speed"))
			{
				Field speed = s_cachedFields.get("speed");
				return speed.getFloat(inEntity);
			}
			else
			{
				Field speed = inEntity.getClass().getDeclaredField("bI");
				speed.setAccessible(true);
				s_cachedFields.put("speed", speed);
				return speed.getFloat(inEntity);
			}
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
			if(s_cachedFields.containsKey("speedModifier"))
			{
				Field speed = s_cachedFields.get("speedModifier");
				return speed.getFloat(inEntity);
			}
			else
			{
				Field speed = inEntity.getClass().getDeclaredField("bQ");
				speed.setAccessible(true);
				s_cachedFields.put("speedModifier", speed);
				return speed.getFloat(inEntity);
			}
		}
		catch(Exception e)
		{
			return 0F;
		}
	}

	/**
	 * Gets the data for the parameters of the classes' constructor
	 *
	 * @param inClass   The class to get the data for
	 * @return          List of data for each parameter in order
	 */
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
							Object value = field.get(inClass);
							parameters.add(new ParameterData(sas.pos(), field.getType().getName(), value, sas.special()));
							break;
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
