package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteTamingFeature extends RemoteFeature implements TamingFeature
{
	protected String m_tamer;

	public RemoteTamingFeature(RemoteEntity inEntity)
	{
		this(inEntity, (String)null);
	}

	@Deprecated
	public RemoteTamingFeature(RemoteEntity inEntity, String inTamer)
	{
		super("TAMING", inEntity);
		this.m_tamer = inTamer;
	}

	public RemoteTamingFeature(RemoteEntity inEntity, Player inTamer)
	{
		this(inEntity, inTamer.getName());
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