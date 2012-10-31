package de.kumpelblase2.remoteentities.nms;

import java.io.IOException;
import net.minecraft.server.*;

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
		}, server.E().getPrivate());
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
