package de.kumpelblase2.remoteentities.api.thinking;

import java.util.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class EnterSightBehaviour implements Behaviour
{
	protected final String NAME = "EnterSight";
	protected int m_tick = 20;
	protected int m_defaultInterval = 20;
	protected Set<String> m_inRange;
	protected final RemoteEntity m_entity;
	protected double m_xRange = 10;
	protected double m_yRange = 5;
	protected double m_zRange = 10;
	
	public EnterSightBehaviour(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_inRange = new HashSet<String>();
	}
	
	public EnterSightBehaviour(RemoteEntity inEntity, double xRange, double yRange, double zRange)
	{
		this(inEntity);
		this.m_xRange = xRange;
		this.m_yRange = yRange;
		this.m_zRange = zRange;
	}
	
	@Override
	public void run()
	{
		this.m_tick--;
		if(this.m_tick <= 0)
		{
			this.m_tick = this.m_defaultInterval;
			this.checkSight();
		}
	}

	private void checkSight()
	{
		Set<String> temp = new HashSet<String>();
		List<Entity> entities = this.m_entity.getBukkitEntity().getNearbyEntities(this.m_xRange, this.m_yRange, this.m_zRange);
		for(Entity entity : entities)
		{
			if(entity instanceof Player)
			{
				Player player = (Player)entity;
				if(!this.m_inRange.contains(player.getName()))
				{
					this.onEnterSight(player);
				}
				temp.add(player.getName());
			}
		}
		this.m_inRange = temp;
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
