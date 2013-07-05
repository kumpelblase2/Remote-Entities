package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will move to a specific location.
 * Once it reached that location the desire will be removed.
 */
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
		return this.getRemoteEntity().getBukkitEntity().getLocation().distanceSquared(this.m_targetLocation) > 1.15;
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g();
	}

	@Override
	public void startExecuting()
	{
		this.m_entity.move(this.m_targetLocation);
	}

	@Override
	public boolean isFinished()
	{
		return !this.canContinue() && this.getRemoteEntity().getBukkitEntity().getLocation().distance(this.m_targetLocation) < 2;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}