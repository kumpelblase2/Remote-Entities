package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class MoveData
{
	private final Location m_locationFrom;
	private final Location m_locationTo;
	private final Block m_above;
	private final Block m_head;
	private final int m_xDiff;
	private final int m_yDiff;
	private final int m_zDiff;
	private boolean m_isValid = true;
	private final Pathfinder m_finder;
	
	public MoveData(Pathfinder inFinder, Location inFrom, Location inTo)
	{
		this.m_finder = inFinder;
		this.m_locationFrom = inFrom;
		this.m_locationTo = inTo;
		this.m_above = inTo.getBlock().getRelative(BlockFace.UP);
		this.m_head = this.m_above.getRelative(BlockFace.UP);
		this.m_xDiff = inTo.getBlockX() - inFrom.getBlockX();
		this.m_yDiff = inTo.getBlockY() - inFrom.getBlockY();
		this.m_zDiff = inTo.getBlockZ() - inFrom.getBlockZ();
	}
	
	public Location getFrom()
	{
		return this.m_locationFrom;
	}
	
	public Location getTo()
	{
		return this.m_locationTo;
	}
	
	public int getXDiff()
	{
		return this.m_xDiff;
	}
	
	public int getYDiff()
	{
		return this.m_yDiff;
	}
	
	public int getZDiff()
	{
		return this.m_zDiff;
	}
	
	public Block getAboveBlock()
	{
		return this.m_above;
	}
	
	public Block getHeadBlock()
	{
		return this.m_head;
	}
	
	public Block getBlock()
	{
		return this.m_locationTo.getBlock();
	}
	
	public Pathfinder getPathfinder()
	{
		return this.m_finder;
	}
	
	public boolean isValid()
	{
		return this.m_isValid;
	}
	
	public void setValid(boolean inValid)
	{
		this.m_isValid = inValid;
	}
}
