package de.kumpelblase2.remoteentities.nms;

import java.io.IOException;
import net.minecraft.server.v1_4_R1.*;

public class RemoteEntityNetworkManager extends NetworkManager
{

	public RemoteEntityNetworkManager(MinecraftServer server) throws IOException
	{
		super(new NullSocket(), "remoteentitiymanager", new Connection()
		{
			@Override
			public boolean a()
			{
				return false;
			}
		}, server.F().getPrivate());
	}

	@Override
	public void a(final Connection nethandler)
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
