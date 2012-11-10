package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class BaseEvent extends Event implements Cancellable
{
	private boolean m_isCancelled = false;
	
	@Override
	public boolean isCancelled()
	{
		return this.m_isCancelled;
	}

	@Override
	public void setCancelled(boolean inCancelled)
	{
		this.m_isCancelled = inCancelled;
	}
}
