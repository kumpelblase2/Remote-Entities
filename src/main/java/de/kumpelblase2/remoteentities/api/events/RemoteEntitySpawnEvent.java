package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

/**
 * Event that gets thrown when an entity gets spawned
 */
public class RemoteEntitySpawnEvent extends RemoteEvent
{
	private static final HandlerList handlers = new HandlerList();
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

	/**
	 * Location the entity should spawn at
	 *
	 * @return location
	 */
	public Location getSpawnLocation()
	{
		return this.m_location;
	}

	/**
	 * Sets the location where the entity should spawn
	 *
	 * @param inLocation location
	 */
	public void setSpawnLocation(Location inLocation)
	{
		this.m_location = inLocation;
	}
}