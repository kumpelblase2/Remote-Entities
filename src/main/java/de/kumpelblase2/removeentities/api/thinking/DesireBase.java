package de.kumpelblase2.removeentities.api.thinking;

import de.kumpelblase2.removeentities.api.RemoteEntity;

public abstract class DesireBase implements Desire
{
	protected final RemoteEntity m_entity;
	protected int m_type = 0;
	protected boolean m_isContinous = true;
	
	public DesireBase(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}

	@Override
	public int getType()
	{
		return this.m_type;
	}
	
	public boolean update()
	{
		return false;
	}
	
	public boolean isContinous()
	{
		return this.m_isContinous;
	}
}
