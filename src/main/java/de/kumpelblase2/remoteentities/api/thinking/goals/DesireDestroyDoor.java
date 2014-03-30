package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R2.*;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

/**
 * Using this desire the entity will try to break a door when it is in the entities path.
 */
public class DesireDestroyDoor extends DesireInteractDoor
{
	protected int m_breakTick;
	protected int m_lastBreak = -1;

	@Deprecated
	public DesireDestroyDoor(RemoteEntity inEntity, boolean inIronDoor)
	{
		super(inEntity, inIronDoor);
	}

	public DesireDestroyDoor(boolean inIronDoor)
	{
		super(inIronDoor);
	}

	@Override
	public boolean shouldExecute()
	{
		if(!super.shouldExecute())
			return false;

		if(!this.getEntityHandle().world.getGameRules().getBoolean("mobGriefing"))
			return false;

		return !this.m_door.f((IBlockAccess)this.getEntityHandle().world, this.m_x, this.m_y, this.m_z);
	}

	@Override
	public void startExecuting()
	{
		super.startExecuting();
		this.m_breakTick = 0;
	}

	@Override
	public boolean canContinue()
	{
		double dist = this.getEntityHandle().e((double)this.m_x, (double)this.m_y, (double)this.m_z);
		return this.m_breakTick <= 240 && !this.m_door.f((IBlockAccess)this.getEntityHandle().world, this.m_x, this.m_y, this.m_z) && dist < 4;
	}

	@Override
	public void stopExecuting()
	{
		super.stopExecuting();
		this.getEntityHandle().world.d(this.getEntityHandle().getId(), this.m_x, this.m_y, this.m_z, -1);
	}

	@Override
	public boolean update()
	{
		super.update();
		if(this.getEntityHandle().aH().nextInt(20) == 0)
			this.getEntityHandle().world.triggerEffect(1010, this.m_x, this.m_y, this.m_z, 0);

		this.m_breakTick++;
		int i = this.m_breakTick / 240 * 10;
		if(i != this.m_lastBreak)
		{
			this.getEntityHandle().world.d(this.getEntityHandle().getId(), this.m_x, this.m_y, this.m_z, i);
			this.m_lastBreak = i;
		}

		World w = this.getEntityHandle().world;
		if(this.m_breakTick == 240 && w.difficulty.a() == 3)
		{
			if (CraftEventFactory.callEntityBreakDoorEvent(this.getEntityHandle(), this.m_x, this.m_y, this.m_z).isCancelled())
			{
                this.update();
                return true;
            }

			w.setAir(this.m_x, this.m_y, this.m_z);
			w.triggerEffect(1012, this.m_x, this.m_y, this.m_z, 0);
			w.triggerEffect(2001, this.m_x, this.m_y, this.m_z, Block.b(this.m_door));
		}
		return true;
	}
}