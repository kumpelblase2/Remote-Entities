package de.kumpelblase2.remoteentities.nms;

import java.io.IOException;
import net.minecraft.server.v1_4_6.MinecraftServer;
import net.minecraft.server.v1_4_6.NetworkManager;
import net.minecraft.server.v1_4_6.Packet;

public class RemoteEntityNetworkManager extends NetworkManager
{

	public RemoteEntityNetworkManager(MinecraftServer server) throws IOException
	{
		super(new NullSocket(), "remoteentitiymanager", new NetHandler()
		{
			@Override
			public boolean a()
			{
				return false;
			}
		}, server.F().getPrivate());
	}

	@Override
	public void a(final NetHandler nethandler)
	{
	}

	@Override
	public void queue(final Packet packet)
	{
	}

	@Override
	public void a(final String s, final Object... aobject)
	{
	}

	@Override
	public void a()
	{
	}
}
