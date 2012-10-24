package de.kumpelblase2.removeentities.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import de.kumpelblase2.removeentities.api.RemoteEntity;

public class RemoteEntityTouchEvent extends RemoteEvent
{
	protected final LivingEntity m_touchingEntity;
	private static HandlerList handlers = new HandlerList();
	
	public RemoteEntityTouchEvent(RemoteEntity inEntity, LivingEntity inTouchingEntity)
	{
		super(inEntity);
		this.m_touchingEntity = inTouchingEntity;
	}
	
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
