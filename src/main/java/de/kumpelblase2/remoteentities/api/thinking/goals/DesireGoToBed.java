package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Location;
import net.minecraft.server.v1_5_R1.Block;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.entities.RemotePlayer;

public class DesireGoToBed extends DesireFindBlockBase
{	
	public DesireGoToBed(RemotePlayer inEntity)
	{
		this(inEntity, 32);
	}
	
	public DesireGoToBed(RemotePlayer inEntity, int inRange)
	{
		super(inEntity, Block.BED.id, inRange);
	}
	
	@Deprecated
	public DesireGoToBed(RemoteEntity inEntity, int inBlockID, int inRange)
	{
		super((RemotePlayer)inEntity, inBlockID, inRange);
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_entity.getHandle().world.u() || ((RemotePlayer)this.m_entity).isSleeping())
			return false;
		
		if(!this.findNearest())
			return false;
		
		return true;
	}
	
	@Override
	public void startExecuting()
	{
		this.m_entity.move(new Location(this.m_entity.getBukkitEntity().getWorld(), this.m_locX, this.m_locY, this.m_locZ));
	}
	
	@Override
	public boolean update()
	{
		if(this.getEntityHandle().getNavigation().f())
		{
			((RemotePlayer)this.m_entity).enterBed(new Location(this.m_entity.getBukkitEntity().getWorld(), this.m_locX, this.m_locY, this.m_locZ));
			return false;
		}

		return true;
	}
}
