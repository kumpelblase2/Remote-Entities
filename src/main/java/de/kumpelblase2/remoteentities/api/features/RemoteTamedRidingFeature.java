package de.kumpelblase2.remoteentities.api.features;

import net.minecraft.server.v1_6_R1.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteTamedRidingFeature extends RemoteFeature implements TamedRidingFeature
{
	@SerializeAs(pos = 2)
	protected boolean m_isRideable;
	@SerializeAs(pos = 3)
	protected int m_rideableChance;
	@SerializeAs(pos = 1)
	protected String m_tamer;

	public RemoteTamedRidingFeature(RemoteEntity inEntity)
	{
		this(inEntity, null);
	}

	public RemoteTamedRidingFeature(RemoteEntity inEntity, String inTamer)
	{
		this(inEntity, inTamer, false, 500);
	}

	public RemoteTamedRidingFeature(RemoteEntity inEntity, boolean inRideable, int inRideableChance)
	{
		this(inEntity, null, inRideable, inRideableChance);
	}

	public RemoteTamedRidingFeature(RemoteEntity inEntity, String inTamer, boolean inRideable, int inRideableChance)
	{
		super("TAMEDRIDING", inEntity);
		this.m_tamer = inTamer;
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
		return this.m_rideableChance;
	}

	@Override
	public void increaseRideableChance(int inChance)
	{
		this.m_rideableChance = MathHelper.a(this.getRideableChance(), 0, 100);
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
		this.m_tamer = inPlayer.getName();
	}

	@Override
	public Player getTamer()
	{
		return Bukkit.getPlayerExact(this.m_tamer);
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}