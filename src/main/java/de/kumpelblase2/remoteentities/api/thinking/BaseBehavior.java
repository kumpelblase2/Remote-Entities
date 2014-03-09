package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public abstract class BaseBehavior implements Behavior
{
	protected String m_name;
	@SerializeAs(pos = 0, special = "entity")
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;

		if(!(o instanceof BaseBehavior))
			return false;

		BaseBehavior that = (BaseBehavior)o;

		if(this.m_entity != null ? !this.m_entity.equals(that.m_entity) : that.m_entity != null)
			return false;

		if(this.m_name != null ? !this.m_name.equals(that.m_name) : that.m_name != null)
			return false;

		return this.getClass().equals(that.getClass());
	}

	@Override
	public int hashCode()
	{
		int result = this.m_name != null ? this.m_name.hashCode() : 0;
		result = 31 * result + (this.m_entity != null ? this.m_entity.hashCode() : 0);
		result = 31 * result * this.getClass().hashCode();
		return result;
	}
}