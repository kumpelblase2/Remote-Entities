package de.kumpelblase2.remoteentities.api.pathfinding;

import java.util.Arrays;
import java.util.List;

public class Path
{
	private List<BlockNode> m_nodes;
	private int m_pos;
	private float m_speed = -1;
	
	public Path(BlockNode... inNodes)
	{
		this(Arrays.asList(inNodes));
	}
	
	public Path(List<BlockNode> inNodes)
	{
		this.m_pos = -1;
		this.m_nodes = inNodes;
	}
	
	public void setCustomSpeed(float inSpeed)
	{
		this.m_speed = inSpeed;
	}
	
	public float getCustomSpeed()
	{
		return this.m_speed;
	}
	
	public boolean hasCustomSpeed()
	{
		return this.getCustomSpeed() != -1;
	}
	
	public BlockNode next()
	{
		this.m_pos++;
		if(this.m_pos >= this.m_nodes.size())
			return null;
		
		return this.m_nodes.get(this.m_pos);
	}
	
	public BlockNode previous()
	{
		this.m_pos--;
		if(this.m_pos < 0)
			return null;
		
		return this.m_nodes.get(this.m_pos);
	}
	
	public BlockNode start()
	{
		this.m_pos = -1;
		return this.next();
	}
	
	public boolean isDone()
	{
		return this.m_pos == this.m_nodes.size() - 1;
	}
}