package de.kumpelblase2.remoteentities.api.thinking;

import java.util.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class EnterSightBehavior extends BaseBehavior
{
	protected int m_tick = 0;
	protected int m_defaultInterval = 10;
	protected Set<String> m_inRange;
	protected double m_xRange = 10;
	protected double m_yRange = 5;
	protected double m_zRange = 10;
	
	public EnterSightBehavior(RemoteEntity inEntity)
	{
		super(inEntity, "EnterSight");
		this.m_inRange = new HashSet<String>();
	}
	
	public EnterSightBehavior(RemoteEntity inEntity, double xRange, double yRange, double zRange)
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

	protected void checkSight()
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
	
	/**
	 * Gets called when a player walks into the range of the entity
	 * 
	 * @param inPlayer player
	 */
	public abstract void onEnterSight(Player inPlayer);

	@Override
	public void onRemove()
	{
		this.m_inRange.clear();
	}
	
	public void setXRange(double inX)
	{
		this.m_xRange = inX;
	}
	
	public void setYRange(double inY)
	{
		this.m_yRange = inY;
	}
	
	public void setZRange(double inZ)
	{
		this.m_zRange = inZ;
	}
	
	public double getXRange()
	{
		return this.m_xRange;
	}
	
	public double getYRange()
	{
		return this.m_yRange;
	}
	
	public double getZRange()
	{
		return this.m_zRange;
	}
}
