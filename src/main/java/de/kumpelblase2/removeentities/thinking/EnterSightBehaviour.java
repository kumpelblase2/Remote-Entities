package de.kumpelblase2.removeentities.thinking;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public abstract class EnterSightBehaviour implements Behaviour
{
	protected final String NAME = "EnterSight";
	protected int m_tick = 20;
	protected int m_defaultInterval = 20;
	protected Set<String> m_inRange;
	protected final RemoteEntity m_entity;
	
	public EnterSightBehaviour(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_inRange = new HashSet<String>();
	}
	
	@Override
	public void run()
	{
		this.m_tick--;
		if(this.m_tick <= 0)
		{
			this.m_tick = this.m_defaultInterval;
			//TODO
		}
	}

	@Override
	public String getName()
	{
		return this.NAME;
	}
	
	public abstract void onEnterSight(Player inPlayer);

	@Override
	public void onRemove()
	{
		this.m_inRange.clear();
	}

	@Override
	public void onAdd()
	{
	}
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
}
