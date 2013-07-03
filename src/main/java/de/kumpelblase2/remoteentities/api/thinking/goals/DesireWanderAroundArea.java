package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R1.Vec3D;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

/**
 * Using this desire the entity will move around randomly inside an area.
 */
public class DesireWanderAroundArea extends DesireWanderAround
{
	@SerializeAs(pos = 1)
	protected int m_Radius;
	@SerializeAs(pos = 2)
	protected Location m_midSpot;

	public DesireWanderAroundArea(RemoteEntity inEntity, int inRadius, Location inMidPoint)
	{
		super(inEntity);
		this.m_Radius = inRadius;
		this.m_midSpot = inMidPoint;
	}

	@Override
	public boolean shouldExecute()
	{
		LivingEntity handle = this.getRemoteEntity().getBukkitEntity();
		if(!WorldUtilities.isInCircle(this.m_midSpot.getX(), this.m_midSpot.getZ(), handle.getLocation().getX(), handle.getLocation().getZ(), this.m_Radius) || super.shouldExecute())
		{
			int tries = 0;
			while(!WorldUtilities.isInCircle(this.m_midSpot.getX(), this.m_midSpot.getZ(), this.m_xPos, this.m_zPos, this.m_Radius) && tries <= 10)
			{
				Vec3D vector = RandomPositionGenerator.a(this.getEntityHandle(), 10, 7);
				if(vector != null)
				{
					this.m_xPos = vector.c;
					this.m_yPos = vector.d;
					this.m_zPos = vector.e;
					Vec3D.a.release(vector);
				}
				tries++;
			}
			return tries <= 10;
		}
		else
			return false;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}