package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class RemoteEvent extends Event implements Cancellable
{
	private boolean m_isCancelled = false;
	protected final RemoteEntity m_remoteEntity;
	
	public RemoteEvent(RemoteEntity inEntity)
	{
		this.m_remoteEntity = inEntity;
	}
	
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public boolean isCancelled()
	{
		return this.m_isCancelled;
	}

	@Override
	public void setCancelled(boolean arg0)
	{
		this.m_isCancelled = arg0;
	}
}
