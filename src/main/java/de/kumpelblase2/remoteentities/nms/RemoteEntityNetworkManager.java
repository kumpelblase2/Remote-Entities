package de.kumpelblase2.remoteentities.nms;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketAddress;
import net.minecraft.server.v1_7_R1.*;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteEntityNetworkManager extends NetworkManager
{

	public RemoteEntityNetworkManager(MinecraftServer server) throws IOException
	{
		super(false);

		this.assignReplacementNetworking();
	}

	private void assignReplacementNetworking()
	{
		ReflectionUtil.setNetworkChannel(this, new NullChannel(null));
		ReflectionUtil.setNetworkAddress(this, new SocketAddress(){});
	}
}