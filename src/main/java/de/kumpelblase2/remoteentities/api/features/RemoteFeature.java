package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteFeature implements Feature
{
	protected final String NAME;
	@SerializeAs(pos = 0, special = "entity")
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

	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}