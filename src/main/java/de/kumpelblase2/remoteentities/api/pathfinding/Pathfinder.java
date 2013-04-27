package de.kumpelblase2.remoteentities.api.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.server.v1_5_R2.EntityLiving;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class Pathfinder
{
	private Set<BlockNode> m_openList;
	private Set<BlockNode> m_closedList;
	private List<MoveChecker> m_checkers;
	private HeuristicType m_heuristicType = HeuristicType.MANHATTAN;
	private final RemoteEntity m_entity;
	private Path m_currentPath;
	public static int MAX_CHECK_TIMEOUT = 200;
	private int m_checked = 0;
	private Path m_lastPath;
	public static Set<Material> transparentMaterial = new HashSet<Material>();
	
	static
	{
		Material[] array = new Material[] { 
			Material.AIR, Material.ACTIVATOR_RAIL, Material.BROWN_MUSHROOM, Material.CARROT, Material.CROPS, Material.DETECTOR_RAIL, Material.DIODE, Material.DIODE_BLOCK_OFF, 
			Material.DIODE_BLOCK_ON, Material.FENCE_GATE, Material.GRASS, Material.LADDER, Material.LEVER, Material.LONG_GRASS, Material.MELON_STEM, Material.NETHER_WARTS, 
			Material.PAINTING, Material.PORTAL, Material.POTATO, Material.PUMPKIN_STEM, Material.RAILS, Material.REDSTONE, Material.RED_ROSE, Material.REDSTONE_COMPARATOR, 
			Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_WIRE, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.SAPLING, 
			Material.SIGN_POST, Material.SKULL, Material.SNOW, Material.TORCH, Material.TRIPWIRE, Material.WALL_SIGN, Material.WOOD_BUTTON, Material.STONE_BUTTON, Material.STONE_PLATE,
			Material.WOOD_PLATE, Material.YELLOW_FLOWER
		};
		transparentMaterial.addAll(Arrays.asList(array));
	}
	
	public Pathfinder(RemoteEntity inEntity)
	{
		this.m_openList = new HashSet<BlockNode>();
		this.m_closedList = new HashSet<BlockNode>();
		this.m_checkers = new ArrayList<MoveChecker>();
		this.m_entity = inEntity;
	}
	
	public PathResult find(Location inStart, Location inEnd)
	{		
		this.m_closedList.clear();
		this.m_openList.clear();
		this.m_checked = 0;
		BlockNode start = new BlockNode(this, inStart);
		BlockNode end = new BlockNode(this, inEnd);
		if(start.equals(end))
		{
			this.m_lastPath = new Path(end);
			return PathResult.SUCCESS;
		}
		
		
		start.calcualteGScore();
		start.calculateHScore(end);
		this.m_openList.add(start);
		BlockNode next = null;
		long startTime = System.currentTimeMillis();
		while(!this.m_closedList.contains(end))
		{
			if(this.m_openList.size() <= 0)
				return PathResult.NO_PATH;
			
			next = this.getNodeWithLowestFScore(end);
			this.m_openList.remove(next);
			this.m_closedList.add(next);
			
			if(end.equals(next))
				break;
			
			List<BlockNode> newNodes = this.getNearNodes(next, end);
			if(this.m_checked >= MAX_CHECK_TIMEOUT)
				return PathResult.MAX_ITERATION;
				
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
		this.m_lastPath = new Path(inOrder);
		return PathResult.SUCCESS;
	}
	
	public boolean moveTo(Location inTo)
	{
		if(!this.m_entity.isSpawned())
			return false;
		
		PathResult result = this.find(this.m_entity.getBukkitEntity().getLocation(), inTo);
		if(result != PathResult.SUCCESS)
			return false;
		
		this.m_currentPath = this.getLastPath();
		return true;
	}
	
	public boolean moveTo(Location inTo, float inSpeed)
	{
		if(!this.moveTo(inTo))
			return false;
		
		this.m_currentPath.setCustomSpeed(inSpeed);
		return true;
	}
	
	protected BlockNode getNodeWithLowestFScore(BlockNode inEnd)
	{		
		BlockNode currentSmallest = null;
		double currentScore = 0;
		
		for(BlockNode n : this.m_openList)
		{
			if(n.getHScore() == -1)
				n.calculateHScore(inEnd);
			
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
	
	protected List<BlockNode> getNearNodes(BlockNode inCurrent, BlockNode inEnd)
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
					if(!this.m_closedList.contains(node) && this.canWalk(inCurrent.getLocation(), node.getLocation()))
					{
						this.m_checked++;
						node.setParent(inCurrent);
						node.calcualteGScore();
						node.calculateHScore(inEnd);
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
	
	public boolean hasPath()
	{
		return this.m_currentPath != null && !this.m_currentPath.isDone();
	}
	
	public Path getCurrentPath()
	{
		return this.m_currentPath;
	}
	
	public void setPath(Path inPath)
	{
		this.m_currentPath = inPath;
	}
	
	public RemoteEntity getEntity()
	{
		return this.m_entity;
	}
	
	public void update()
	{
		if(!this.hasPath() || !this.getEntity().isSpawned())
			return;
		
		BlockNode next = this.m_currentPath.next();
		if(next == null)
		{
			this.cancelPath();
			return;
		}
		
		EntityLiving entity = this.getEntity().getHandle();
		double yDist = next.getY() - entity.locY;
		if(yDist > 0)
			entity.getControllerJump().a();
		
		entity.getControllerMove().a(next.getX(), next.getY(), next.getZ(), (this.m_currentPath.hasCustomSpeed() ? this.m_currentPath.getCustomSpeed() : this.m_entity.getSpeed()));
		
		if(this.m_currentPath.isDone())
			this.cancelPath();
	}
	
	public void cancelPath()
	{
		this.m_currentPath = null;
	}
	
	public Path getLastPath()
	{
		return this.m_lastPath;
	}
	
	public static Pathfinder getDefaultPathfinder(RemoteEntity inEntity)
	{
		Pathfinder p = new Pathfinder(inEntity);
		
		p.addChecker(new AirChecker());
		p.addChecker(new JumpChecker());
		p.addChecker(new JumpDownChecker());
		p.addChecker(new WallChecker());
		p.addChecker(new DoorOpenChecker());
		
		return p;
	}
	
	public static boolean isTransparent(Block inBlock)
	{
		return isTransparent(inBlock.getType());
	}
	
	public static boolean isTransparent(Material inType)
	{
		return transparentMaterial.contains(inType);
	}
}