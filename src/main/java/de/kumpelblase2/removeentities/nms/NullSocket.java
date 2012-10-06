package de.kumpelblase2.removeentities.nms;

import java.io.*;
import java.net.Socket;

class NullSocket extends Socket
{
	private final byte[] buffer = new byte[0];
	
	@Override
	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(this.buffer);
	}
	
	@Override
	public OutputStream getOutputStream()
	{
		return new ByteArrayOutputStream(1);
	}
}
