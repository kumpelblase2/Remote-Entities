package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class NotAnOcelotException extends Exception
{
	public NotAnOcelotException()
	{
		super("Entity is not an ocelot.");
	}
}
