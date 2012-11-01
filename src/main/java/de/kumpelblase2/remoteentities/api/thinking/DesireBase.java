package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

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
		return true;
	}
	
	public boolean isContinous()
	{
		return this.m_isContinous;
	}
	
	@Override
	public void stopExecuting()
	{
	}
	
	@Override
	public void startExecuting()
	{
	}
	
	@Override
	public boolean canContinue()
	{
		return this.shouldExecute();
	}
	
	@Override
	public void setType(int inType)
	{
		this.m_type = inType;
	}
}
