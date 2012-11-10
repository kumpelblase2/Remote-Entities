package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import net.minecraft.server.World;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireDestroyDoor extends DesireInteractDoor
{
	protected int m_breakTick;
	protected int m_lastBreak = -1;
	
	public DesireDestroyDoor(RemoteEntity inEntity, boolean inIronDoor)
	{
		super(inEntity, inIronDoor);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return !super.shouldExecute() ? false : ! this.m_door.a_(this.getRemoteEntity().getHandle().world, this.m_x, this.m_y, this.m_z);
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
		double dist = this.getRemoteEntity().getHandle().e((double)this.m_x, (double)this.m_y, (double)this.m_z);
		return this.m_breakTick <= 240 && !this.m_door.a_(this.getRemoteEntity().getHandle().world, this.m_x, this.m_y, this.m_z) && dist < 4;
	}
	
	@Override
	public void stopExecuting()
	{
		super.stopExecuting();
		this.getRemoteEntity().getHandle().world.g(this.getRemoteEntity().getHandle().id, this.m_x, this.m_y, this.m_z, -1);
	}
	
	@Override
	public boolean update()
	{
		super.update();
		if(this.getRemoteEntity().getHandle().aA().nextInt(20) == 0)
			this.getRemoteEntity().getHandle().world.triggerEffect(1010, this.m_x, this.m_y, this.m_z, 0);
		
		this.m_breakTick++;
		int i = (int)(this.m_breakTick / 240 * 10);
		if(i != this.m_lastBreak)
		{
			this.getRemoteEntity().getHandle().world.g(this.getRemoteEntity().getHandle().id, this.m_x, this.m_y, this.m_z, i);
			this.m_lastBreak = i;
		}
		
		World w = this.getRemoteEntity().getHandle().world;
		if(this.m_breakTick == 240 && w.difficulty == 3)
		{
			if (CraftEventFactory.callEntityBreakDoorEvent(this.getRemoteEntity().getHandle(), this.m_x, this.m_y, this.m_z).isCancelled()) {
                this.update();
                return true;
            }
			
			w.setTypeId(this.m_x, this.m_y, this.m_z, 0);
			w.triggerEffect(1012, this.m_x, this.m_y, this.m_z, 0);
			w.triggerEffect(2001, this.m_x, this.m_y, this.m_z, this.m_door.id);
		}
		return true;
	}
}
