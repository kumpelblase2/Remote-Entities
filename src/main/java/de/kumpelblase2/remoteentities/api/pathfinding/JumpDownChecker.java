package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.block.BlockFace;

public class JumpDownChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(!inData.isValid())
			return;
		
		if(inData.getYDiff() == -1)
		{
			if(!Pathfinder.isTransparent(inData.getAboveBlock()) || !Pathfinder.isTransparent(inData.getHeadBlock()) || !Pathfinder.isTransparent(inData.getHeadBlock().getRelative(BlockFace.UP)))
			{
				inData.setValid(false);
				return;
			}
		}
	}
}
