package de.kumpelblase2.remoteentities.api.features;

import net.minecraft.server.v1_6_R1.MathHelper;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteRidingFeature extends RemoteFeature implements RidingFeature
{
	protected boolean m_isRideable = false;
	protected int m_rideableChance = 500;

	public RemoteRidingFeature(RemoteEntity inEntity)
	{
		super("RIDING", inEntity);
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
}