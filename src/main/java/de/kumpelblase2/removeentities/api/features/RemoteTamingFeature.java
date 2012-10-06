package de.kumpelblase2.removeentities.api.features;

import org.bukkit.entity.Player;
import de.kumpelblase2.removeentities.api.RemoteEntity;

public class RemoteTamingFeature extends RemoteFeature implements TamingFeature
{
	private Player m_tamer;
	
	public RemoteTamingFeature(String inName, RemoteEntity inEntity)
	{
		super(inName, inEntity);
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
