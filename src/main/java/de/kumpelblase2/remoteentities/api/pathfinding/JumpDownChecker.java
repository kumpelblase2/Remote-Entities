package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.block.BlockFace;

public class JumpDownChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(inData.getYDiff() == -1)
		{
			if(!inData.getAboveBlock().isEmpty() || !inData.getHeadBlock().isEmpty() || !inData.getHeadBlock().getRelative(BlockFace.UP).isEmpty())
			{
				inData.setValid(false);
				return;
			}
		}
	}
}
