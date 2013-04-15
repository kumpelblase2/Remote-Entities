package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Gate;

public class JumpChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(inData.getYDiff() == 1)
		{
			if(inData.getXDiff() == 0 && inData.getZDiff() == 0 && !inData.getFrom().getBlock().isLiquid())
			{
				inData.setValid(false);
				return;
			}
			
			Block aboveHead = inData.getFrom().getBlock().getRelative(BlockFace.UP, 3);
			if(!aboveHead.isEmpty() && !aboveHead.isLiquid())
			{
				inData.setValid(false);
				return;
			}
			
			Block to = inData.getTo().getBlock();
			if(to.getType() == Material.FENCE || (to.getType() == Material.FENCE_GATE && !((Gate)to.getState().getData()).isOpen()))
			{
				inData.setValid(false);
				return;
			}
		}
	}
}
