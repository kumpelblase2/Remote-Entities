package de.kumpelblase2.remoteentities.api.pathfinding;

public class AirChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(inData.getYDiff() >= 0)
		{
			if(!Pathfinder.isTransparent(inData.getBlock()))
				inData.setValid(false);
		}
	}
}