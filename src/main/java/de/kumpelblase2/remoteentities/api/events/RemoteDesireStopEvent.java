package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;

public class RemoteDesireStopEvent extends RemoteEvent
{
	private static HandlerList handlers = new HandlerList();
	private final DesireItem m_desire;
	
	public RemoteDesireStopEvent(RemoteEntity inEntity, DesireItem inDesire)
	{
		super(inEntity);
		this.m_desire = inDesire;
	}
	
	/**
	 * Gets the desire that is about to get stopped.
	 * 
	 * @return	Stopping desire
	 */
	public Desire getDesire()
	{
		return this.m_desire.getDesire();
	}
	
	/**
	 * Gets the priority of the desire.
	 * 
	 * @return	Desire priority
	 */
	public int getPriority()
	{
		return this.m_desire.getPriority();
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