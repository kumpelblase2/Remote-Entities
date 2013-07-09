package de.kumpelblase2.remoteentities.nms;

import java.io.IOException;
import java.lang.reflect.Field;
import net.minecraft.server.v1_6_R2.*;

public class RemoteEntityNetworkManager extends NetworkManager
{

	public RemoteEntityNetworkManager(MinecraftServer server) throws IOException
	{
		super(server.getLogger(), new NullSocket(), "remoteentitiymanager", new Connection()
		{
			@Override
			public boolean a()
			{
				return false;
			}
		}, server.H().getPrivate());
		try
		{
			Field field = NetworkManager.class.getDeclaredField("n");
			field.setAccessible(true);
			field.set(this, false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
	public void d()
	{
	}

	@Override
	public void a()
	{
	}

	@Override
	public int e()
	{
		return 0;
	}
}