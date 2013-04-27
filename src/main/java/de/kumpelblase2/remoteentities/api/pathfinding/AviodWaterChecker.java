package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.Material;


public class AviodWaterChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(!inData.isValid())
			return;
		
		if(inData.getBlock().getType() == Material.WATER || inData.getBlock().getType() == Material.STATIONARY_WATER)
		{
			inData.setValid(false);
			return;
		}
		
		if(inData.getAboveBlock().getType() == Material.WATER || inData.getAboveBlock().getType() == Material.STATIONARY_WATER)
		{
			inData.setValid(false);
			return;
		}
	}
}
