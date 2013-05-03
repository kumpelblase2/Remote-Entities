package de.kumpelblase2.remoteentities.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

/**
 * Event that gets fired when an entity got touched by another
 */
public class RemoteEntityTouchEvent extends RemoteEvent
{
	protected final LivingEntity m_touchingEntity;
	private static final HandlerList handlers = new HandlerList();
	
	public RemoteEntityTouchEvent(RemoteEntity inEntity, LivingEntity inTouchingEntity)
	{
		super(inEntity);
		this.m_touchingEntity = inTouchingEntity;
	}
	
	/**
	 * Entity that's touching the RemoteEntity
	 * 
	 * @return entity
	 */
	public LivingEntity getTouchingEntity()
	{
		return this.m_touchingEntity;
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
