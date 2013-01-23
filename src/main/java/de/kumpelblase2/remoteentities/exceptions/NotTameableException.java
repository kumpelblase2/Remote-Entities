package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class NotTameableException extends RuntimeException
{
	public NotTameableException()
	{
		super("Entity is not tameable.");
	}
}
