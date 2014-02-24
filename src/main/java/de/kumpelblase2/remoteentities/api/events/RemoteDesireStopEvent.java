package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;

public class RemoteDesireStopEvent extends RemoteEvent
{
	private static final HandlerList handlers = new HandlerList();
	private final DesireItem m_desire;

	private final StopReason m_stopReason;

	public RemoteDesireStopEvent(DesireItem inDesire, StopReason inReason)
	{
		super(inDesire.getDesire().getRemoteEntity());
		this.m_desire = inDesire;
		this.m_stopReason = inReason;
	}

	public RemoteDesireStopEvent(DesireItem inDesire)
	{
		this(inDesire, StopReason.CANT_CONTINUE);
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

	/**
	 * Gets the reason the desire was stopped.
	 *
	 * @return  Stop reason
	 */
	public StopReason getStopReason()
	{
		return m_stopReason;
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

	public static enum StopReason
	{
		/**
		 * The desire currently can't continue or has finished.
		 */
		CANT_CONTINUE,
		/**
		 * Desire got removed.
		 */
		REMOVE,
		/**
		 * Another desire with a higher priority from the same type wants to get executed
		 */
		LOWER_PRIORITY
	}
}