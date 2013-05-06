package de.kumpelblase2.remoteentities.api.events;

import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.pathfinding.*;
import org.bukkit.event.*;

public class RemoteAsyncPathFindEvent extends RemoteEvent
{
	private static final HandlerList handlers = new HandlerList();
	private Path m_path;
	private final  boolean m_isAsync;

	public RemoteAsyncPathFindEvent(RemoteEntity inEntity, Path inPath)
	{
		this(inEntity, inPath, false);
	}

	public RemoteAsyncPathFindEvent(RemoteEntity inEntity, Path inPath, boolean inAsync)
	{
		super(inEntity);
		this.m_path = inPath;
		this.m_isAsync = inAsync;
	}

	public Path getPath()
	{
		return this.m_path;
	}

	public void setPath(Path inPath)
	{
		this.m_path = inPath;
	}

	public boolean isAsync()
	{
		return this.m_isAsync;
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
