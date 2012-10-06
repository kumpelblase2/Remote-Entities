package de.kumpelblase2.removeentities.api.features;

import de.kumpelblase2.removeentities.api.RemoteEntity;

public class RemoteFeature implements Feature
{
	protected final String NAME;
	protected final RemoteEntity m_entity;
	
	public RemoteFeature(String inName, RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.NAME = inName;
	}
	
	@Override
	public String getName()
	{
		return this.NAME;
	}

	@Override
	public RemoteEntity getEntity()
	{
		return this.m_entity;
	}
}
