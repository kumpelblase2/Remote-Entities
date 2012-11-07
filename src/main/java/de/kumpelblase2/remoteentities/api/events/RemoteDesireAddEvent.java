package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.Desire;

public class RemoteDesireAddEvent extends RemoteEvent
{
	private Desire m_desire;
	private static HandlerList handlers = new HandlerList();
	private int m_priority;
	
	public RemoteDesireAddEvent(RemoteEntity inEntity, Desire inDesire, int inPriority)
	{
		super(inEntity);
		this.m_desire = inDesire;
		this.m_priority = inPriority;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
	public Desire getDesire()
	{
		return this.m_desire;
	}
	
	public void setDesire(Desire inDesire)
	{
		this.m_desire = inDesire;
	}
	
	public int getPriority()
	{
		return this.m_priority;
	}
	
	public void setPriority(int inPriority)
	{
		this.m_priority = inPriority;
	}
}