package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteTamedRidingFeature extends RemoteFeature implements TamedRidingFeature
{
	protected boolean m_isRideable = false;
	protected Player m_tamer;

	public RemoteTamedRidingFeature(RemoteEntity inEntity)
	{
		super("TAMEDRIDING", inEntity);
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
	public boolean isTamed()
	{
		return this.m_tamer != null;
	}

	@Override
	public void untame()
	{
		this.m_tamer = null;
	}

	@Override
	public void tame(Player inPlayer)
	{
		this.m_tamer = inPlayer;
	}

	@Override
	public Player getTamer()
	{
		return this.m_tamer;
	}
}