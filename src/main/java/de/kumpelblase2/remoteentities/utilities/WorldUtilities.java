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
	 * @param m_xPos	middle x coordinate of circle
	 * @param m_zPos	middle y coordinate of circle
	 * @param d			point x value
	 * @param e			point y value
	 * @param inRadius	radius
	 * @return			true if inside, false if not
	 */
	public static boolean isInCircle(double m_xPos, double m_zPos, double d, double e, int inRadius)
	{
		double newX = (m_xPos - d);
		double newY = (m_zPos - e);
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
