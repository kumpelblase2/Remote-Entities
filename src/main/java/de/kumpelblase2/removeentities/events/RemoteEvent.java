package de.kumpelblase2.removeentities.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public class RemoteEvent extends Event implements Cancellable
{
	public static final HandlerList handlers = new HandlerList();
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
	public HandlerList getHandlers()
	{
		return handlers;
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
