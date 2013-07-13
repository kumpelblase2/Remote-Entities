package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteAgeFeature extends RemoteFeature implements AgeFeature
{
	@SerializeAs(pos = 1)
	protected int m_age;

	public RemoteAgeFeature()
	{
		this(null);
	}

	public RemoteAgeFeature(int inAge)
	{
		this(null, inAge);
	}

	public RemoteAgeFeature(RemoteEntity inEntity)
	{
		this(inEntity, 0);
	}

	public RemoteAgeFeature(RemoteEntity inEntity, int inAge)
	{
		super("AGE", inEntity);
		this.m_age = inAge;
	}

	@Override
	public int getAge()
	{
		return this.m_age;
	}

	@Override
	public void setAge(int inAge)
	{
		this.m_age = inAge;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}