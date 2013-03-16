package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R1.Vec3D;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

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
		if(super.shouldExecute())
		{
			int tries = 0;
			while(!WorldUtilities.isInCircle(this.m_xPos, this.m_zPos, this.m_midSpot.getX(), this.m_midSpot.getZ(), this.m_Radius) && tries <= 10)
			{
				Vec3D vector = RandomPositionGenerator.a(this.getEntityHandle(), 10, 7);
				if(vector != null)
				{
					this.m_xPos = vector.c;
					this.m_yPos = vector.d;
					this.m_zPos = vector.e;
					return true;
				}
				tries++;
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}