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

	public RemoteEntityInteractEvent(RemoteEntity inEntity, Player inInteractor)
	{
		super(inEntity);
		this.m_interactor = inInteractor;
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