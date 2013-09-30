package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public abstract class BaseBehavior implements Behavior
{
	protected String m_name;
	@SerializeAs(pos = 0 , special = "entity")
	protected final RemoteEntity m_entity;

	public BaseBehavior(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
	}

	@Override
	public void run()
	{
	}

	@Override
	public String getName()
	{
		return this.m_name;
	}

	@Override
	public void onAdd()
	{
	}

	@Override
	public void onRemove()
	{
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}