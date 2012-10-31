package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireOpenDoor extends DesireInteractDoor
{
	protected boolean m_closeDoor;
	protected int m_closeTick;
	
	public DesireOpenDoor(RemoteEntity inEntity, boolean inCloseDoor, boolean inIronDoor)
	{
		super(inEntity, inIronDoor);
		this.m_closeDoor = inCloseDoor;
	}
	
	@Override
	public boolean canContinue()
	{
		return this.m_closeDoor && this.m_closeTick > 0 && super.canContinue();
	}
	
	@Override
	public void startExecuting()
	{
		this.m_closeTick = 20;
		this.m_door.setDoor(this.getRemoteEntity().getHandle().world, this.m_x, this.m_y, this.m_z, true);
	}
	
	@Override
	public void stopExecuting()
	{
		if(this.m_closeDoor)
			this.m_door.setDoor(this.getRemoteEntity().getHandle().world, this.m_x, this.m_y, this.m_z, false);
	}
	
	@Override
	public boolean update()
	{
		this.m_closeTick--;
		return super.update();
	}
}
