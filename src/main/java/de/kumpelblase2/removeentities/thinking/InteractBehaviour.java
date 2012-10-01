package de.kumpelblase2.removeentities.thinking;

import org.bukkit.entity.Player;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public abstract class InteractBehaviour implements Behaviour
{
	protected final String NAME = "Interact";
	protected final RemoteEntity m_entity;
	
	public InteractBehaviour(RemoteEntity inEntity)
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
