package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteEntityDespawnEvent extends RemoteEvent
{
	private static HandlerList handlers = new HandlerList();
	private DespawnReason m_reason;
	
	public RemoteEntityDespawnEvent(RemoteEntity inEntity)
	{
		this(inEntity, DespawnReason.CUSTOM);
	}
	
	public RemoteEntityDespawnEvent(RemoteEntity inEntity, DespawnReason inReason)
	{
		super(inEntity);
		this.m_reason = inReason;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public DespawnReason getReason()
	{
		return this.m_reason;
	}
}
