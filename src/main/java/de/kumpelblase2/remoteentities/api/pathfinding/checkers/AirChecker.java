package de.kumpelblase2.remoteentities.api.pathfinding.checkers;

import de.kumpelblase2.remoteentities.api.pathfinding.MoveData;
import de.kumpelblase2.remoteentities.api.pathfinding.Pathfinder;

public class AirChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(inData.getYDiff() >= 0)
		{
			if(!Pathfinder.isTransparent(inData.getBlock()) && !Pathfinder.isLiquid(inData.getBlock()))
				inData.setValid(false);
		}
	}
}