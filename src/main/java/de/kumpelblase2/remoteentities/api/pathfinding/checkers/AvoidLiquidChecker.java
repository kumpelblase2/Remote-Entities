package de.kumpelblase2.remoteentities.api.pathfinding.checkers;

import de.kumpelblase2.remoteentities.api.pathfinding.*;

public class AvoidLiquidChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(!inData.isValid())
			return;

		if(Pathfinder.isLiquid(inData.getBlock()) || Pathfinder.isLiquid(inData.getAboveBlock()))
			inData.setValid(false);
	}
}