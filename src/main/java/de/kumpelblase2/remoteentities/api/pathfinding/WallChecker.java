package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.block.Block;

public class WallChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(inData.getYDiff() == 0 || inData.getYDiff() == 1)
		{
			Block above = inData.getAboveBlock();
			Block head = inData.getHeadBlock();
			
			if(!above.isEmpty())
			{
				inData.setValid(false);
				return;
			}
			
			if(!head.isEmpty())
			{
				inData.setValid(false);
				return;
			}
		}
		
		if(inData.getXDiff() != 0 && inData.getZDiff() != 0)
		{
			if(!inData.getPathfinder().canWalk(inData.getFrom(), inData.getFrom().add(inData.getXDiff(), 0, 0)) || !inData.getPathfinder().canWalk(inData.getFrom(), inData.getFrom().add(0, 0, inData.getZDiff())))
			{
				inData.setValid(false);
				return;
			}
		}
		
		if(!inData.isValid())
			inData.setValid(true);
	}
}