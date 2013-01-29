package de.kumpelblase2.remoteentities.api.thinking;

import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.PathEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.entities.RemoteBaseEntity;

import java.util.ArrayList;

public abstract class DesireBase implements Desire
{
	protected final RemoteEntity m_entity;
	protected int m_type = 0;
	protected boolean m_isContinous = true;
	
	public DesireBase(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
	
	public EntityLiving getEntityHandle()
	{
		if(this.m_entity == null)
			return null;
		
		return this.getRemoteEntity().getHandle();
	}

	@Override
	public int getType()
	{
		return this.m_type;
	}
	
	public boolean update()
	{
		return true;
	}
	
	public boolean isContinuous()
	{
		return this.m_isContinous;
	}
	
	@Override
	public void stopExecuting()
	{
	}
	
	@Override
	public void startExecuting()
	{
	}
	
	@Override
	public boolean canContinue()
	{
		return this.shouldExecute();
	}
	
	@Override
	public void setType(int inType)
	{
		this.m_type = inType;
	}
	
	public void movePath(PathEntity inPath, float inSpeed)
	{
		if(this.getRemoteEntity() instanceof RemoteBaseEntity)
			((RemoteBaseEntity)this.getRemoteEntity()).moveWithPath(inPath, inSpeed);
		else
			this.getEntityHandle().getNavigation().a(inPath, inSpeed);
	}
}
