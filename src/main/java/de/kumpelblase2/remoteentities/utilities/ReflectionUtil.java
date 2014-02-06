package de.kumpelblase2.remoteentities.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.*;
import net.minecraft.util.io.netty.channel.Channel;
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
			Field field = getOrRegisterField(inEntity.getClass(), inSelectorName);
			field.set(inEntity, inNewSelector);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Registers custom entity class at the native minecraft entity enum.
	 * Automatically clears internal maps first @see ReflectionUtil#clearEntityType(String, int)
	 *
	 * @param inClass	class of the entity
	 * @param inName	minecraft entity name
	 * @param inID		minecraft entity id
	 */
	public static void registerEntityType(Class<?> inClass, String inName, int inID)
	{
		try
		{
			clearEntityType(inName, inID);
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;

            Method a = getNMSClassByName("EntityTypes").getDeclaredMethod("a", args);
            a.setAccessible(true);

            a.invoke(a, inClass, inName, inID);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
	}

	/**
	 * Clears the entity name and entity id from the EntityTypes internal c and e map to allow registering of those names with different values.
	 * The other maps are not touched and stay as they are.
	 *
	 * @param inName    The internal name of the entity
	 * @param inID      The internal id of the entity
	 */
	public static void clearEntityType(String inName, int inID)
	{
		try
		{
			Field cMap = getOrRegisterNMSField("EntityTypes", "c");
			Field eMap = getOrRegisterNMSField("EntityTypes", "e");
			((Map)cMap.get(null)).remove(inName);
			((Map)eMap.get(null)).remove(inID);
		}
		catch(Exception e)
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
			Field jump = getOrRegisterNMSField("EntityLiving", "bd");
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

	/**
	 * Gets the current minecraft revision
	 *
	 * @return  The revision as string in the format "X.X_RX"
	 */
	public static String getMinecraftRevision()
	{
		Class serverClass = Bukkit.getServer().getClass();
		String remaining = serverClass.getPackage().getName().replace("org.bukkit.craftbukkit.", "");
		return remaining.split("\\.")[0];
	}

	/**
	 * Gets the nms class with the given name
	 *
	 * @param inName    The internal name of the class
	 * @return          The class
	 */
	public static Class<?> getNMSClassByName(String inName)
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

	/**
	 * Sets the new socket channel of the given manager.
	 *
	 * @param inManager The manager to change the channel of
	 * @param inChannel The new channel
	 */
	public static void setNetworkChannel(Object inManager, Channel inChannel)
	{
		try
		{
			Field channel = getOrRegisterNMSField("NetworkManager", "k");
			channel.set(inManager, inChannel);
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * Sets the network address of the given network manager.
	 *
	 * @param inManager The manager to change the address of
	 * @param inAddress The new address
	 */
	public static void setNetworkAddress(Object inManager, SocketAddress inAddress)
	{
		try
		{
			Field address = getOrRegisterNMSField("NetworkManager", "l");
			address.set(inManager, inAddress);
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * Gets a declared field of the given class and caches it.
	 * If a field is not cached it will attempt to get it from the given class.
	 *
	 * @param inSource  The class which has the field
	 * @param inField   The field name
	 * @return          The field
	 */
	public static Field getOrRegisterField(Class<?> inSource, String inField)
	{
		Field field;
		try
		{
			if(s_cachedFields.containsKey(inField))
				field = s_cachedFields.get(inField);
			else
			{
				field = inSource.getDeclaredField(inField);
				field.setAccessible(true);
				s_cachedFields.put(inField, field);
			}

			return field;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// We split up in two methods so we don't cause much overhead because otherwise we'd need to search for the nms class every time when calling the method
	// instead of just once when we need it
	static Field getOrRegisterNMSField(String inNMSClass, String inField)
	{
		Field field;
		try
		{
			if(s_cachedFields.containsKey(inField))
				field = s_cachedFields.get(inField);
			else
			{
				field = getNMSClassByName(inNMSClass).getDeclaredField(inField);
				field.setAccessible(true);
				s_cachedFields.put(inField, field);
			}

			return field;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}