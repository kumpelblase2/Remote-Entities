package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;

public class RemoteDesireStartEvent extends RemoteEvent
{
	private static HandlerList handlers = new HandlerList();
	private DesireItem m_desire;
	
	public RemoteDesireStartEvent(RemoteEntity inEntity, DesireItem inDesire)
	{
		super(inEntity);
		this.m_desire = inDesire;
	}
	
	public Desire getDesire()
	{
		return this.m_desire.getDesire();
	}
	
	public int getPriority()
	{
		return this.m_desire.getPriority();
	}
	
	public void setDesire(DesireItem inDesire)
	{
		this.m_desire = inDesire;
	}
	
	public DesireItem getDesireItem()
	{
		return this.m_desire;
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
}
