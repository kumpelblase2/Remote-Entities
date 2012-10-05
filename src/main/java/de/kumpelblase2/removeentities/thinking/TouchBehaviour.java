package de.kumpelblase2.removeentities.thinking;

import org.bukkit.entity.Player;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public abstract class TouchBehaviour implements Behaviour
{
	protected final String NAME = "Touch";
	protected final RemoteEntity m_entity;
	
	public TouchBehaviour(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public void run()
	{
	}
	
	public abstract void onTouch(Player inPlayer);

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
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
}
