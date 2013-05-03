package de.kumpelblase2.remoteentities.api.pathfinding;

public class AvoidLiquidChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(!inData.isValid())
			return;
		
		if(Pathfinder.isLiquid(inData.getBlock()))
		{
			inData.setValid(false);
			return;
		}

		if(Pathfinder.isLiquid(inData.getAboveBlock()))
			inData.setValid(false);
	}
}