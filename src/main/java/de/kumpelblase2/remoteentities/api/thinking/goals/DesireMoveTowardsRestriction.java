package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;

public class DesireMoveTowardsRestriction extends DesireBase
{
	protected double m_x;
	protected double m_y;
	protected double m_z;
	
	public DesireMoveTowardsRestriction(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getRemoteEntity().getHandle().aB())
			return false;
		else
		{
			ChunkCoordinates chunkCoords = this.getRemoteEntity().getHandle().aC();
			Vec3D vec = RandomPositionGenerator.a(this.getRemoteEntity().getHandle(), 16, 7, Vec3D.a().create(chunkCoords.x, chunkCoords.y, chunkCoords.z));
			if(vec == null)
				return false;
			else
			{
				this.m_x = vec.a;
				this.m_y = vec.b;
				this.m_z = vec.c;
				return true;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.getRemoteEntity().getHandle().getNavigation().f();
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().getHandle().getNavigation().a(this.m_x, this.m_y, this.m_z, this.getRemoteEntity().getSpeed());
	}
}
