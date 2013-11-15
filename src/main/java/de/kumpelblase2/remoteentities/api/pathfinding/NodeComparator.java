package de.kumpelblase2.remoteentities.api.pathfinding;

import java.util.Comparator;

public class NodeComparator implements Comparator<BlockNode>
{
	@Override
	public int compare(BlockNode inBlockNode, BlockNode inBlockNode2)
	{
		return (int)(inBlockNode.getFScore() - inBlockNode2.getFScore());
	}
}