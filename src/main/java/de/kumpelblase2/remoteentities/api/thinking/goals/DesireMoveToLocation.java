package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.OneTimeDesire;
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
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getRemoteEntity().getBukkitEntity().getLocation().distanceSquared(this.m_targetLocation) > 1.15;
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
		this.m_entity.move(this.m_targetLocation);
		return true;
	}
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}