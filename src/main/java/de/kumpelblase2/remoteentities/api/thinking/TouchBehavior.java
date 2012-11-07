package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class TouchBehavior implements Behavior
{
	protected final String NAME = "Touch";
	protected final RemoteEntity m_entity;
	
	public TouchBehavior(RemoteEntity inEntity)
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
