package de.kumpelblase2.remoteentities.api.pathfinding;

public class WallChecker implements MoveChecker
{
	@Override
	public void checkMove(MoveData inData)
	{
		if(!inData.isValid())
			return;
		
		if(inData.getYDiff() >= 0)
		{
			if(!Pathfinder.isTransparent(inData.getAboveBlock()) || !Pathfinder.isTransparent(inData.getHeadBlock()))
			{
				inData.setValid(false);
				return;
			}
		}
		
		if(inData.getXDiff() != 0 && inData.getZDiff() != 0)
		{
			if(!inData.getPathfinder().canWalk(inData.getFrom(), inData.getFrom().add(inData.getXDiff(), 0, 0)) || !inData.getPathfinder().canWalk(inData.getFrom(), inData.getFrom().add(0, 0, inData.getZDiff())))
				inData.setValid(false);
		}
	}
}