package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Location;
import net.minecraft.server.v1_5_R2.ChunkCoordinates;
import net.minecraft.server.v1_5_R2.Vec3D;
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
		if(this.getEntityHandle() == null || this.getEntityHandle().aL())
			return false;
		else
		{
			ChunkCoordinates chunkCoords = this.getEntityHandle().aM();
			Vec3D vec = RandomPositionGenerator.a(this.getEntityHandle(), 16, 7, this.getEntityHandle().world.getVec3DPool().create(chunkCoords.x, chunkCoords.y, chunkCoords.z));
			if(vec == null)
				return false;
			else
			{
				this.m_x = vec.c;
				this.m_y = vec.d;
				this.m_z = vec.e;
				return true;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.getEntityHandle().getNavigation().f();
	}
	
	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_x, this.m_y, this.m_z));
	}
}
