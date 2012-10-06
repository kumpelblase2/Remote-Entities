package de.kumpelblase2.removeentities.api;

import de.kumpelblase2.removeentities.entities.*;
import de.kumpelblase2.removeentities.utilities.EnumChange;
import net.minecraft.server.EntityLiving;

public enum RemoteEntityType
{
	Human(RemotePlayer.class, RemotePlayerEntity.class, true), 
	Zombie(RemoteZombie.class, RemoteZombieEntity.class, false),
	Creeper(RemoteCreeper.class, RemoteCreeperEntity.class, false);
	
	private Class<? extends EntityLiving> m_entityClass;
	private Class<? extends RemoteEntity> m_remoteClass;
	private boolean m_isNamed = false;
	
	private RemoteEntityType(Class<? extends RemoteEntity> inRemoteClass, Class<? extends EntityLiving> inEntityClass)
	{
		this.m_entityClass = inEntityClass;
		this.m_remoteClass = inRemoteClass;
	}
	
	private RemoteEntityType(Class<? extends RemoteEntity> inRemoteClass, Class<? extends EntityLiving> inEntityClass, boolean inNamed)
	{
		this(inRemoteClass, inEntityClass);
		this.m_isNamed = inNamed;
	}
	
	public Class<? extends RemoteEntity> getRemoteClass()
	{
		return this.m_remoteClass;
	}
	
	public Class<? extends EntityLiving> getEntityClass()
	{
		return this.m_entityClass;
	}
	
	public boolean isNamed()
	{
		return this.m_isNamed;
	}
	
	public static void addType(String inName, Class<? extends EntityLiving> inEntityClass, Class<? extends RemoteEntity> inRemoteClass, boolean isNamed)
	{
		EnumChange.addEnum(RemoteEntityType.class, inName, new Class<?>[] { Class.class, Class.class, boolean.class }, new Object[] { inRemoteClass, inEntityClass, isNamed });
	}
	
	public static void removeType(String inName)
	{
		EnumChange.removeEnum(RemoteEntityType.class, inName);
	}
	
	public static void replaceType(String inName, Class<? extends EntityLiving> inEntityClass, Class<? extends RemoteEntity> inRemoteClass, boolean isNamed)
	{
		removeType(inName);
		addType(inName, inEntityClass, inRemoteClass, isNamed);
	}
	
	public static RemoteEntityType getByEntityClass(Class<? extends EntityLiving> inEntityClass)
	{
		for(RemoteEntityType type : values())
		{
			if(type.getEntityClass().equals(inEntityClass))
				return type;
		}
		return null;
	}
	
	public static RemoteEntityType getByRemoteClass(Class<? extends RemoteEntity> inRemoteClass)
	{
		for(RemoteEntityType type : values())
		{
			if(type.getRemoteClass().equals(inRemoteClass))
				return type;
		}
		return null;
	}
}
