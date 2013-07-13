package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R2.Block;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.entities.RemotePlayer;

/**
 * Using this desire the player will try to find next bed and will go to sleep at night.
 */
public class DesireGoToBed extends DesireFindBlockBase
{
	@Deprecated
	public DesireGoToBed(RemotePlayer inEntity)
	{
		this(inEntity, 32);
	}

	@Deprecated
	public DesireGoToBed(RemotePlayer inEntity, int inRange)
	{
		super(inEntity, Block.BED.id, inRange);
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Deprecated
	public DesireGoToBed(RemoteEntity inEntity, int inBlockID, int inRange)
	{
		super(inEntity, inBlockID, inRange);
	}

	public DesireGoToBed()
	{
		this(32);
	}

	public DesireGoToBed(int inRange)
	{
		super(Block.BED.id, inRange);
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Deprecated
	public DesireGoToBed(int inBlockID, int inRange)
	{
		super(inBlockID, inRange);
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_entity.getHandle().world.v() || ((RemotePlayer)this.m_entity).isSleeping())
			return false;

		return this.findNearest();
	}

	@Override
	public void startExecuting()
	{
		this.m_entity.move(new Location(this.m_entity.getBukkitEntity().getWorld(), this.m_locX, this.m_locY, this.m_locZ));
	}

	@Override
	public boolean update()
	{
		if(this.getNavigation().g())
		{
			((RemotePlayer)this.m_entity).enterBed(new Location(this.m_entity.getBukkitEntity().getWorld(), this.m_locX, this.m_locY, this.m_locZ));
			return false;
		}

		return true;
	}
}