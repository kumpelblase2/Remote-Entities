package de.kumpelblase2.removeentities.utilities;

public class WorldUtilities
{
	public static boolean isInCircle(double m_xPos, double m_zPos, double d, double e, int inRadious)
	{
		double newX = (m_xPos - d);
		double newY = (m_zPos - e);
		return newX * newX + newY * newY < inRadious * inRadious;
	}
}
