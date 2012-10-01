package de.kumpelblase2.removeentities.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public class RemoteEntityInteractEvent extends RemoteEvent
{
	protected final Player m_interactor;
	private static HandlerList handlers = new HandlerList();
	
	public RemoteEntityInteractEvent(RemoteEntity inEntity, Player inInteractor)
	{
		super(inEntity);
		this.m_interactor = inInteractor;
	}
	
	public Player getInteractor()
	{
		return this.m_interactor;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
