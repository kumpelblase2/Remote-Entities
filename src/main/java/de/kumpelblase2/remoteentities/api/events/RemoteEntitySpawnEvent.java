package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteEntitySpawnEvent extends RemoteEvent
{
	private static HandlerList handlers = new HandlerList();
	private Location m_location;
	
	public RemoteEntitySpawnEvent(RemoteEntity inEntity, Location inLocation)
	{
		super(inEntity);
		this.m_location = inLocation;
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
	
	public Location getSpawnLocation()
	{
		return this.m_location;
	}
	
	public void setSpawnLocation(Location inLocation)
	{
		this.m_location = inLocation;
	}
}