package de.kumpelblase2.remoteentities.utilities;

import java.lang.reflect.*;
import java.util.*;
import sun.reflect.*;

public final class EnumChange
{
	private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

	private static void setFailsafeFieldValue(final Field field, final Object target, final Object value) throws NoSuchFieldException, IllegalAccessException
	{
		// let's make the field accessible
		field.setAccessible(true);
		// next we change the modifier in the Field instance to
		// not be final anymore, thus tricking reflection into
		// letting us modify the static final field
		final Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);
		// blank out the final bit in the modifiers int
		modifiers &= ~Modifier.FINAL;
		modifiersField.setInt(field, modifiers);
		final FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
		fa.set(target, value);
	}

	private static void blankField(final Class<?> enumClass, final String fieldName) throws NoSuchFieldException, IllegalAccessException
	{
		for(final Field field : Class.class.getDeclaredFields())
			if(field.getName().contains(fieldName))
			{
				AccessibleObject.setAccessible(new Field[] { field }, true);
				setFailsafeFieldValue(field, enumClass, null);
				break;
			}
	}

	private static void cleanEnumCache(final Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException
	{
		blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
	}

	private static ConstructorAccessor getConstructorAccessor(final Class<?> enumClass, final Class<?>[] additionalParameterTypes) throws NoSuchMethodException
	{
		final Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
		parameterTypes[0] = String.class;
		parameterTypes[1] = int.class;
		System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
		return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
	}

	private static Object makeEnum(final Class<?> enumClass, final String value, final int ordinal, final Class<?>[] additionalTypes, final Object[] additionalValues) throws Exception
	{
		final Object[] parms = new Object[additionalValues.length + 2];
		parms[0] = value;
		parms[1] = Integer.valueOf(ordinal);
		System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
		return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
	}

	/**
	 * Add an enum instance to the enum class given as argument
	 * 
	 * @param <T>
	 *            the type of the enum (implicit)
	 * @param enumType
	 *            the class of the enum to be modified
	 * @param enumName
	 *            the name of the new enum instance to be added to the class.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>>void addEnum(final Class<T> enumType, final String enumName, final Class<?>[] constructorTypes, final Object[] constructorValues)
	{
		// 0. Sanity checks
		if(!Enum.class.isAssignableFrom(enumType))
			throw new RuntimeException("class " + enumType + " is not an instance of Enum");
		// 1. Lookup "$VALUES" holder in enum class and get previous enum instances
		Field valuesField = null;
		final Field[] fields = enumType.getDeclaredFields();
		for(final Field field : fields)
		{
			if(field.getName().contains("$VALUES"))
			{
				valuesField = field;
				break;
			}
		}
		if(valuesField == null)
			return;
		
		AccessibleObject.setAccessible(new Field[] { valuesField }, true);
		try
		{
			// 2. Copy it
			final T[] previousValues = (T[])valuesField.get(enumType);
			final List<T> values = new ArrayList<T>(Arrays.asList(previousValues));
			// 3. build new enum
			final T newValue = (T)makeEnum(enumType, // The target enum class
					enumName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
					values.size(), constructorTypes, // could be used to pass values to the enum constuctor if needed
					constructorValues); // could be used to pass values to the enum constuctor if needed
			// 4. add new value
			values.add(newValue);
			// 5. Set new values field
			setFailsafeFieldValue(valuesField, null, values.toArray((T[])Array.newInstance(enumType, 0)));
			// 6. Clean enum cache
			cleanEnumCache(enumType);
		}
		catch(final Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Removes an enum instance from the enum class given as argument
	 * 
	 * @param <T>
	 *            the type of the enum (implicit)
	 * @param enumType
	 *            the class of the enum to be modified
	 * @param enumName
	 *            the name of the new enum instance to be removed to the class.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>>void removeEnum(final Class<T> enumType, final String enumName)
	{
		if(!Enum.class.isAssignableFrom(enumType))
			throw new RuntimeException("class " + enumType + " is not an instance of Enum");
		Field valuesField = null;
		final Field[] fields = enumType.getDeclaredFields();
		for(final Field field : fields)
		{
			if(field.getName().contains("$VALUES"))
			{
				valuesField = field;
				break;
			}
		}
		if(valuesField == null)
			return;
		AccessibleObject.setAccessible(new Field[] { valuesField }, true);
		try
		{
			final T[] previousValues = (T[])valuesField.get(enumType);
			final List<T> values = new ArrayList<T>(Arrays.asList(previousValues));
			values.remove(Enum.valueOf(enumType, enumName));
			setFailsafeFieldValue(valuesField, null, values.toArray((T[])Array.newInstance(enumType, 0)));
			cleanEnumCache(enumType);
		}
		catch(final Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
