package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.EntityLiving;

public enum RemoteEntityType
{
	Human(RemotePlayer.class, RemotePlayerEntity.class, true);
	
	private Class<? extends EntityLiving> m_entityClass;
	private Class<? extends RemoteBaseEntity> m_remoteClass;
	private boolean m_isNamed = false;
	
	private RemoteEntityType(Class<? extends RemoteBaseEntity> inRemoteClass, Class<? extends EntityLiving> inEntityClass)
	{
		this.m_entityClass = inEntityClass;
		this.m_remoteClass = inRemoteClass;
	}
	
	private RemoteEntityType(Class<? extends RemoteBaseEntity> inRemoteClass, Class<? extends EntityLiving> inEntityClass, boolean inNamed)
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
}
