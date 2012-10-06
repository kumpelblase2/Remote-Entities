package de.kumpelblase2.removeentities.api.thinking.goals;

import net.minecraft.server.Vec3D;
import org.bukkit.Location;
import de.kumpelblase2.removeentities.api.RemoteEntity;
import de.kumpelblase2.removeentities.nms.RandomPositionGenerator;
import de.kumpelblase2.removeentities.utilities.WorldUtilities;

public class DesireWanderAroundArea extends DesireWanderAround
{
	private int m_Radius;
	private Location m_midSpot;
	
	public DesireWanderAroundArea(RemoteEntity inEntity, int inRadius, Location inMidPoint)
	{
		super(inEntity);
		this.m_Radius = inRadius;
		this.m_midSpot = inMidPoint;
	}
	
	public boolean a()
	{
		if(super.a())
		{
			int tries = 0;
			while(!WorldUtilities.isInCircle(this.m_xPos, this.m_zPos, this.m_midSpot.getX(), this.m_midSpot.getZ(), this.m_Radius) && tries <= 10)
			{
				Vec3D vector = RandomPositionGenerator.a(this.getRemoteEntity().getHandle(), 10, 7);
				if(vector != null)
				{
					this.m_xPos = vector.a;
					this.m_yPos = vector.b;
					this.m_zPos = vector.c;
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
}
