package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

/**
 * Gets fired when a player interacts with an entity
 */
public class RemoteEntityInteractEvent extends RemoteEvent
{
	protected final Player m_interactor;
	private static final HandlerList handlers = new HandlerList();
	private final boolean m_isLeftClick;

	public RemoteEntityInteractEvent(RemoteEntity inEntity, Player inInteractor)
	{
		this(inEntity, inInteractor, true);
	}

	public RemoteEntityInteractEvent(RemoteEntity inEntity, Player inInteractor, boolean inLeftClick)
	{
		super(inEntity);
		this.m_interactor = inInteractor;
		this.m_isLeftClick = inLeftClick;
	}

	/**
	 * Player that interacted with the entity
	 *
	 * @return player
	 */
	public Player getInteractor()
	{
		return this.m_interactor;
	}

	/**
	 * Returns whether the player used left or right click to interact with the entity
	 *
	 * @return  true for left click, false for right
	 */
	public boolean isLeftClick()
	{
		return this.m_isLeftClick;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
}