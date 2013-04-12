package de.kumpelblase2.remoteentities.api.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;

public class Pathfinder
{
	private Set<BlockNode> m_openList;
	private Set<BlockNode> m_closedList;
	private BlockNode m_start;
	private BlockNode m_end;
	private List<MoveChecker> m_checkers;
	private HeuristicType m_heuristicType = HeuristicType.MANHATTAN;
	
	public Pathfinder()
	{
		this.m_openList = new HashSet<BlockNode>();
		this.m_closedList = new HashSet<BlockNode>();
		this.m_checkers = new ArrayList<MoveChecker>();
	}
	
	public Path find(Location inStart, Location inEnd)
	{
		this.m_start = new BlockNode(this, inStart);
		this.m_end = new BlockNode(this, inEnd);
		if(this.m_start.equals(this.m_end))
			return new Path(this.m_start);
		
		
		this.m_start.calcualteGScore();
		this.m_start.calculateHScore(this.m_end);
		this.m_openList.add(this.m_start);
		BlockNode next = null;
		while(!this.m_closedList.contains(this.m_end))
		{
			if(this.m_openList.size() <= 0)
				return null;
			
			next = this.getNodeWithLowestFScore();
			this.m_openList.remove(next);
			this.m_closedList.add(next);
			
			if(this.m_end.equals(next))
				break;
			
			List<BlockNode> newNodes = this.getNearNodes(next);
			for(BlockNode n : newNodes)
			{
				n.setParent(next);
				if(!this.m_openList.add(n))
				{
					BlockNode old = this.getFromOpenList(n.toString());
					if(old.getGScore() > n.getGScore())
					{
						old.setParent(next);
						old.calcualteGScore();
					}
				}
			}
		}
		
		ArrayList<BlockNode> inOrder = new ArrayList<BlockNode>();
		inOrder.add(next);
		while((next = next.getParent()) != null)
		{
			inOrder.add(next);
		}
		
		Collections.reverse(inOrder);
		return new Path(inOrder);
	}
	
	protected BlockNode getNodeWithLowestFScore()
	{
		BlockNode currentSmallest = null;
		double currentScore = 0;
		
		for(BlockNode n : this.m_openList)
		{
			if(n.getHScore() == -1)
				n.calculateHScore(this.m_end);
			
			if(n.getGScore() == -1)
				n.calcualteGScore();
			
			if(currentScore == 0 || n.getFScore() < currentScore)
			{
				currentScore = n.getFScore();
				currentSmallest = n;
			}
		}
		
		return currentSmallest;
	}
	
	public List<BlockNode> getNearNodes(BlockNode inCurrent)
	{
		ArrayList<BlockNode> nodes = new ArrayList<BlockNode>(26);
		for(int x = -1; x <= 1; x++)
		{
			for(int y = -1; y <= 1; y++)
			{
				for(int z = -1 ; z <= 1; z++)
				{
					if(x == 0 && y == 0 && z == 0)
						continue;
					
					BlockNode node = new BlockNode(this, inCurrent.getLocation().add(x, y, z));
					if(node.equals(this))
						continue;
					
					if(this.canWalk(inCurrent.getLocation(), node.getLocation()) && !this.m_closedList.contains(node))
					{
						node.setParent(inCurrent);
						node.calcualteGScore();
						node.calculateHScore(this.m_end);
						nodes.add(node);
					}
				}
			}
		}
		return nodes;
	}
	
	protected BlockNode getFromOpenList(String inHash)
	{
		for(BlockNode n : this.m_openList)
		{
			if(n.toString().equals(inHash))
				return n;
		}
		return null;
	}
	
	public HeuristicType getHeuristicType()
	{
		return this.m_heuristicType;
	}
	
	public void setHeuristicType(HeuristicType inType)
	{
		this.m_heuristicType = inType;
	}
	
	public boolean canWalk(Location inFrom, Location inTo)
	{
		MoveData data = new MoveData(this, inFrom, inTo);
		for(MoveChecker checker : this.m_checkers)
		{
			checker.checkMove(data);
		}
		return data.isValid();
	}
	
	public void addChecker(MoveChecker inChecker)
	{
		this.m_checkers.add(inChecker);
	}
	
	public void addChecker(MoveChecker inChecker, int inPriority)
	{
		this.m_checkers.add(inPriority, inChecker);
	}
	
	public boolean hasChecker(Class<? extends MoveChecker> inType)
	{
		return this.getChecker(inType) != null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends MoveChecker> T getChecker(Class<T> inType)
	{
		for(MoveChecker c : this.m_checkers)
		{
			if(c.getClass().isAssignableFrom(inType))
				return (T)c;
		}
		
		return null;
	}
	
	public void removeChecker(Class<? extends MoveChecker> inType)
	{
		Iterator<MoveChecker> it = this.m_checkers.iterator();
		while(it.hasNext())
		{
			if(it.next().getClass().isAssignableFrom(inType))
				it.remove();
		}
	}
	
	public static Pathfinder getDefaultPathfinder()
	{
		Pathfinder p = new Pathfinder();
		
		p.addChecker(new JumpChecker());
		p.addChecker(new JumpDownChecker());
		p.addChecker(new WallChecker());
		p.addChecker(new DoorOpenChecker());
		
		return p;
	}
}