package de.kumpelblase2.remoteentities.api.pathfinding;

public class AirChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(inData.getYDiff() >= 0)
		{
			if(inData.getBlock().isEmpty())
			{
				inData.setValid(false);
				return;
			}
		}
	}
}
