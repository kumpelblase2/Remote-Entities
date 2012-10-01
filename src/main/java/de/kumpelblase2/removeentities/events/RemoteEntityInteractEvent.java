package de.kumpelblase2.removeentities.events;

import org.bukkit.entity.Player;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public class RemoteEntityInteractEvent extends RemoteEvent
{
	protected final Player m_interactor;
	
	public RemoteEntityInteractEvent(RemoteEntity inEntity, Player inInteractor)
	{
		super(inEntity);
		this.m_interactor = inInteractor;
	}
	
	public Player getInteractor()
	{
		return this.m_interactor;
	}
}
