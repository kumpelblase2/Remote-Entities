package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteAgeFeature extends RemoteFeature implements AgeFeature
{
	protected int m_age = 0;
	
	public RemoteAgeFeature(RemoteEntity inEntity)
	{
		super("AGE", inEntity);
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
}
