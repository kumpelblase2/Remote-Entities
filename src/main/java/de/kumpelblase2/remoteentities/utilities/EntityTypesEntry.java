package de.kumpelblase2.remoteentities.utilities;

import java.lang.reflect.Field;
import java.util.Map;

public class EntityTypesEntry
{
	private final String m_name;
	@SuppressWarnings("rawtypes")
	private final Class m_class;
	private final Integer m_id;

	@SuppressWarnings("rawtypes")
	public EntityTypesEntry(String inName, Class inClass, Integer inID)
	{
		this.m_name = inName;
		this.m_class = inClass;
		this.m_id = inID;
	}

	public void restore()
	{
		ReflectionUtil.registerEntityType(this.m_class, this.m_name, this.m_id);
	}

	@SuppressWarnings("rawtypes")
	public static EntityTypesEntry fromEntity(String inEntity)
	{
		try
		{
			Class entityTypes = ReflectionUtil.getNMSClassByName("EntityTypes");
			Field classMap = ReflectionUtil.getOrRegisterField(entityTypes, "c");
			Field idMap = ReflectionUtil.getOrRegisterField(entityTypes, "f");
			Class entityClass = (Class)((Map)classMap.get(null)).get(inEntity);
			Integer id = (Integer)((Map)idMap.get(null)).get(entityClass);
			return new EntityTypesEntry(inEntity, entityClass, id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public int getID()
	{
		return this.m_id;
	}

	@SuppressWarnings("rawtypes")
	public Class getEntityClass()
	{
		return this.m_class;
	}

	public String getName()
	{
		return this.m_name;
	}
}
