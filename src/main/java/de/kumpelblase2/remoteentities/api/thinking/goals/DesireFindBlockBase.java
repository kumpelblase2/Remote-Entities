package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R3.EntityLiving;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * This is a base for desires which will search for a specific block such as DesireGoToBed.
 */
public abstract class DesireFindBlockBase extends DesireBase
{
	@SerializeAs(pos = 1)
	private int m_blockID;
	protected int m_locX;
	protected int m_locY;
	protected int m_locZ;
	@SerializeAs(pos = 2)
	private int m_range;

	@Deprecated
	public DesireFindBlockBase(RemoteEntity inEntity, int inBlockID)
	{
		this(inEntity, inBlockID, 32);
	}

	@Deprecated
	public DesireFindBlockBase(RemoteEntity inEntity, int inBlockID, int inRange)
	{
		super(inEntity);
		this.m_blockID = inBlockID;
		this.m_range = inRange;
	}

	public DesireFindBlockBase(int inBlockID)
	{
		this(inBlockID, 32);
	}

	public DesireFindBlockBase(int inBlockID, int inRange)
	{
		super();
		this.m_blockID = inBlockID;
		this.m_range = inRange;
	}

	protected boolean findNearest()
	{
		EntityLiving entity = this.getEntityHandle();
		double shortestDistance = Double.MAX_VALUE;
		Location shortest = null;
		for(int x = (int)(entity.locX - this.m_range); x < entity.locX + this.m_range; x++)
		{
			for(int y = (int)(entity.locY - 8); y < entity.locY + 8; y++)
			{
				for(int z = (int)(entity.locZ - this.m_range); z < entity.locZ + this.m_range; z++)
				{
					if(entity.world.getTypeId(x, y, z) == this.m_blockID)
					{
						double dist = entity.e(x, y, z);
						if(dist < shortestDistance)
						{
							shortestDistance = dist;
							shortest = new Location(entity.world.getWorld(), x, y, z);
						}
					}
				}
			}
		}

		if(shortest == null)
			return false;

		this.m_locX = shortest.getBlockX();
		this.m_locY = shortest.getBlockY();
		this.m_locZ = shortest.getBlockZ();
		return true;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}