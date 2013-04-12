package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class BlockNode
{
	private final Pathfinder m_finder;
	private Location m_loc;
	private BlockNode m_parent;
	private String m_hash;
	private double m_g = -1;
	private double m_h = -1;
	
	public static double COST_NORMAL = 1.0d;
	public static double COST_UP = 1.1d;
	public static double COST_DIAGONAL = 1.6d;
	public static double COST_DIAGONAL_UP = 1.8d;
	
	public BlockNode(Pathfinder inFinder, Location inLoc)
	{
		this.m_finder = inFinder;
		this.setLocation(inLoc);
	}
	
	public int getX()
	{
		return this.m_loc.getBlockX();
	}
	
	public int getY()
	{
		return this.m_loc.getBlockY();
	}
	
	public int getZ()
	{
		return this.m_loc.getBlockZ();
	}
	
	public World getWorld()
	{
		return this.m_loc.getWorld();
	}
	
	public Location getLocation()
	{
		return this.m_loc.clone();
	}
	
	public void setLocation(Location inLocation)
	{
		this.m_loc = inLocation;
		this.m_hash = new StringBuilder().append(this.getX()).append(this.getY()).append(this.getZ()).toString(); 
	}
	
	public BlockNode getParent()
	{
		return this.m_parent;
	}
	
	public void setParent(BlockNode inParent)
	{
		this.m_parent = inParent;
	}
	
	@Override
	public String toString()
	{
		return this.m_hash;
	}
	
	public double getHScore()
	{
		return this.m_h;
	}
	
	public double getGScore()
	{
		return this.m_g;
	}
	
	public double getFScore()
	{
		return this.getGScore() + this.getHScore();
	}
	
	public void calculateHScore(BlockNode inEnd)
	{
		switch(this.m_finder.getHeuristicType())
		{
			case MANHATTAN:
				this.m_h = Math.abs(inEnd.getX() - this.getX()) + Math.abs(inEnd.getY() - this.getY()) + Math.abs(inEnd.getZ() + this.getZ());
				break;
			case EUCLIDEAN:
				this.m_h = Math.sqrt(Math.pow(this.getX() - inEnd.getX(), 2) + Math.pow(this.getY() - inEnd.getY(), 2) + Math.pow(this.getZ() - inEnd.getZ(), 2));
				break;
		}
	}
	
	public void calcualteGScore()
	{
		BlockNode parent = this.getParent();
		BlockNode current = this;
		double currentCost = 0;
		while((parent = current.getParent()) != null)
		{
			int dX = Math.abs(parent.getX() - current.getX());
			int dY = Math.abs(parent.getY() - current.getY());
			int dZ = Math.abs(parent.getZ() - current.getZ());
			
			if(dX == 1 && dZ == 1)
			{
				if(dY == 1)
					currentCost += BlockNode.COST_DIAGONAL_UP;
				else
					currentCost += BlockNode.COST_DIAGONAL;
			}
			else
			{
				if(dY == 1)
					currentCost += BlockNode.COST_UP;
				else
					currentCost += BlockNode.COST_NORMAL;
			}
			
			current = parent;
		}
		
		this.m_g = currentCost;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		
		return o.toString().equals(this.toString());
	}
	
	public boolean isSolid()
	{
		return !this.getLocation().getBlock().isEmpty();
	}
	
	public boolean canJump()
	{
		return this.getLocation().getBlock().getRelative(BlockFace.UP, 3).isEmpty();
	}
	
	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}
}