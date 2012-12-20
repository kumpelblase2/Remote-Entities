package de.kumpelblase2.remoteentities.utilities;

import net.minecraft.server.v1_4_5.Entity;
import net.minecraft.server.v1_4_5.MathHelper;
import net.minecraft.server.v1_4_5.Village;

public class WorldUtilities
{
	/**
	 * Checks if a position is inside a circle
	 * 
	 * @param m_xPos	middle x coordinate of circle
	 * @param m_zPos	middle y coordinate of circle
	 * @param d			point x value
	 * @param e			point y value
	 * @param inRadious	radius
	 * @return			true if inside, false if not
	 */
	public static boolean isInCircle(double m_xPos, double m_zPos, double d, double e, int inRadious)
	{
		double newX = (m_xPos - d);
		double newY = (m_zPos - e);
		return newX * newX + newY * newY < inRadious * inRadious;
	}
	
	/**
	 * Gets the closest village to an entity
	 * 
	 * @param inEntity	entity
	 * @return			village
	 */
	public static Village getClosestVillage(Entity inEntity)
	{
		return inEntity.world.villages.getClosestVillage(MathHelper.floor(inEntity.locX), MathHelper.floor(inEntity.locY), MathHelper.floor(inEntity.locZ), 32);
	}
}
