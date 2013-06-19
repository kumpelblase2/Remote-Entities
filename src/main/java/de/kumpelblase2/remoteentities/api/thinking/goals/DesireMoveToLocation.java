package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class DesireMoveToLocation extends DesireBase implements OneTimeDesire
{
	@SerializeAs(pos = 1)
	private Location m_targetLocation;
	
	public DesireMoveToLocation(RemoteEntity inEntity, Location inTargetLocation)
	{
		super(inEntity);
		this.m_targetLocation = inTargetLocation;
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	@Override
	public boolean shouldExecute()
	{
		return !((CraftLivingEntity)this.getRemoteEntity().getBukkitEntity()).getHandle().getNavigation().f();
	}
	
	@Override
	public boolean canContinue()
	{
		return this.shouldExecute();
	}
	
	public void startExecuting()
	{
		this.m_entity.move(this.m_targetLocation);
	}
	
	@Override
	public boolean update()
	{
		return this.m_entity.move(this.m_targetLocation);
	}
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}

	@Override
	public boolean isFinished()
	{
		return !this.shouldExecute();
	}
}
