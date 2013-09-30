package de.kumpelblase2.remoteentities.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import org.bukkit.Bukkit;
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
	public static void replaceGoalSelector(Object inEntity, String inSelectorName, Object inNewSelector)
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

            Method a = Class.forName("net.minecraft.server." + RemoteEntities.getMinecraftRevision() + ".EntityTypes").getDeclaredMethod("a", args);
            a.setAccessible(true);

            a.invoke(a, inClass, name, inID);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
	}

	/**
	 * Checks if the entity is jumping.
	 *
	 * @param inEntity  The entity to check
	 * @return          True if it is, otherwise false
	 */
	public static boolean isJumping(Object inEntity)
	{
		try
		{
			Field jump;
			if(s_cachedFields.containsKey("jump"))
				jump = s_cachedFields.get("jump");
			else
			{
				jump = Class.forName("net.minecraft.server." + RemoteEntities.getMinecraftRevision() + ".EntityLiving").getDeclaredField("bd");
				jump.setAccessible(true);
				s_cachedFields.put("jump", jump);
			}

			return jump.getBoolean(inEntity);
		}
		catch(Exception e)
		{
			return false;
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
							parameters.add(new ParameterData(Math.max(0, sas.pos() - 1), field.getType().getName(), value, sas.special()));
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

	public static String getMinecraftRevision()
	{
		Class serverClass = Bukkit.getServer().getClass();
		String remaining = serverClass.getPackage().getName().replace("org.bukkit.craftbukkit.", "");
		return remaining.split("\\.")[0];
	}

	public static Class getNMSClassByName(String inName)
	{
		try
		{
			return Class.forName("net.minecraft.server." + RemoteEntities.getMinecraftRevision() + "." + inName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}