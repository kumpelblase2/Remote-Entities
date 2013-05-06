package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;

public class RemoteDesireStartEvent extends RemoteEvent
{
	private static final HandlerList handlers = new HandlerList();
	private DesireItem m_desire;
	
	public RemoteDesireStartEvent(RemoteEntity inEntity, DesireItem inDesire)
	{
		super(inEntity);
		this.m_desire = inDesire;
	}
	
	/**
	 * Gets the desire is about to start.
	 * 
	 * @return	Starting desire
	 */
	public Desire getDesire()
	{
		return this.m_desire.getDesire();
	}
	
	/**
	 * Gets the priority the desire is at.
	 * 
	 * @return Desire priority
	 */
	public int getPriority()
	{
		return this.m_desire.getPriority();
	}
	
	/**
	 * Sets the desire that should be started instead.
	 * 
	 * @param inDesire	Desire to start.
	 */
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
