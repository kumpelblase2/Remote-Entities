package de.kumpelblase2.remoteentities.utilities;

import net.minecraft.server.v1_5_R3.*;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.pathfinding.BlockNode;

public class WorldUtilities
{
	/**
	 * Checks if a position is inside a circle
	 *
	 * @param m_midXPos	middle x coordinate of circle
	 * @param m_midZPos	middle y coordinate of circle
	 * @param inPointX	point x value
	 * @param inPointZ	point y value
	 * @param inRadius	radius
	 * @return			true if inside, false if not
	 */
	public static boolean isInCircle(double m_midXPos, double m_midZPos, double inPointX, double inPointZ, int inRadius)
	{
		double newX = (m_midXPos - inPointX);
		double newY = (m_midZPos - inPointZ);
		return newX * newX + newY * newY < inRadius * inRadius;
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

	public static Vector addEntityWidth(RemoteEntity inEntity, BlockNode inNode)
	{
		Vector vec = new Vector(inNode.getX(), inNode.getY(), inNode.getZ());
		double width = ((int)(inEntity.getHandle().width + 1)) * 0.5d;
		vec.add(new Vector(width, 0, width));
		return vec;
	}

	/**
	 * Gets the NMS entity from a bukkit entity.
	 *
	 * @param inEntity  The bukkit entity
	 * @return          NMS entity
	 */
	public static EntityLiving getNMSEntity(org.bukkit.entity.LivingEntity inEntity)
	{
		return ((CraftLivingEntity)inEntity).getHandle();
	}
}
