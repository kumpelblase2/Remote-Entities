package de.kumpelblase2.remoteentities.utilities;

import net.minecraft.server.Entity;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Village;

public class WorldUtilities
{
	public static boolean isInCircle(double m_xPos, double m_zPos, double d, double e, int inRadious)
	{
		double newX = (m_xPos - d);
		double newY = (m_zPos - e);
		return newX * newX + newY * newY < inRadious * inRadious;
	}
	
	public static Village getClosestVillage(Entity inEntity)
	{
		return inEntity.world.villages.getClosestVillage(MathHelper.floor(inEntity.locX), MathHelper.floor(inEntity.locY), MathHelper.floor(inEntity.locZ), 32);
	}
}
