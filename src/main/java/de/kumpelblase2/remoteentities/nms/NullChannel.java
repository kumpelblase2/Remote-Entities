package de.kumpelblase2.remoteentities.nms;

import java.net.SocketAddress;
import net.minecraft.util.io.netty.channel.*;

class NullChannel extends AbstractChannel
{
	protected final ChannelConfig m_channelConfig = new DefaultChannelConfig(this);

	protected NullChannel(Channel parent)
	{
		super(parent);
	}

	@Override
	public ChannelConfig config()
	{
		return this.m_channelConfig;
	}

	@Override
	public boolean isOpen()
	{
		return true;
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public ChannelMetadata metadata()
	{
		return null;
	}

	@Override
	protected AbstractUnsafe newUnsafe()
	{
		return null;
	}

	@Override
	protected boolean isCompatible(EventLoop inEventExecutors)
	{
		return true;
	}

	@Override
	protected SocketAddress localAddress0()
	{
		return null;
	}

	@Override
	protected SocketAddress remoteAddress0()
	{
		return null;
	}

	@Override
	protected void doBind(SocketAddress inSocketAddress) throws Exception
	{
	}

	@Override
	protected void doDisconnect() throws Exception
	{
	}

	@Override
	protected void doClose() throws Exception
	{
	}

	@Override
	protected void doBeginRead() throws Exception
	{
	}

	@Override
	protected void doWrite(ChannelOutboundBuffer inChannelOutboundBuffer) throws Exception
	{
	}
}