package de.kumpelblase2.remoteentities.api.features;

import net.minecraft.server.v1_6_R1.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteRidingFeature extends RemoteFeature implements RidingFeature
{
	@SerializeAs(pos = 1)
	protected boolean m_isRideable;
	@SerializeAs(pos = 2)
	protected int m_rideableChance;

	public RemoteRidingFeature(RemoteEntity inEntity)
	{
		this(inEntity, false, 500);
	}

	public RemoteRidingFeature(RemoteEntity inEntity, boolean inRideable, int inRideableChance)
	{
		super("RIDING", inEntity);
		this.m_isRideable = inRideable;
		this.m_rideableChance = inRideableChance;
	}

	@Override
	public boolean isPreparedToRide()
	{
		return this.m_isRideable;
	}

	@Override
	public void setRideable(boolean inStatus)
	{
		this.m_isRideable = inStatus;
	}

	@Override
	public int getRideableChance()
	{
		return m_rideableChance;
	}

	@Override
	public void increaseRideableChance(int inChance)
	{
		this.m_rideableChance = MathHelper.a(this.getRideableChance(), 0, 100);
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}