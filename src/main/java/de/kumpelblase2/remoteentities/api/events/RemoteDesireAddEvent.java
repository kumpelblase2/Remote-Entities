package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.Desire;

/**
 * Event that gets fired when a desire is about to get added to an entity
 */
public class RemoteDesireAddEvent extends RemoteEvent
{
	private Desire m_desire;
	private static final HandlerList handlers = new HandlerList();
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
	
	/**
	 * Gets the desire that is about to get added
	 * 
	 * @return desire
	 */
	public Desire getDesire()
	{
		return this.m_desire;
	}
	
	/**
	 * Sets the desire that should be added instead
	 * 
	 * @param inDesire desire
	 */
	public void setDesire(Desire inDesire)
	{
		this.m_desire = inDesire;
	}
	
	/**
	 * Gets the priority the desire should have
	 * 
	 * @return priority
	 */
	public int getPriority()
	{
		return this.m_priority;
	}
	
	/**
	 * Sets the priority the desire should have
	 * 
	 * @param inPriority priority
	 */
	public void setPriority(int inPriority)
	{
		this.m_priority = inPriority;
	}
}