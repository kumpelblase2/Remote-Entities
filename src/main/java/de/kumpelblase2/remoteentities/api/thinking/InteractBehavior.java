package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class InteractBehavior implements Behavior
{
	protected final String NAME = "Interact";
	protected final RemoteEntity m_entity;
	
	public InteractBehavior(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public void run()
	{
	}

	@Override
	public String getName()
	{
		return this.NAME;
	}

	@Override
	public void onAdd()
	{
	}

	@Override
	public void onRemove()
	{
	}

	public abstract void onInteract(Player inPlayer);
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
}
