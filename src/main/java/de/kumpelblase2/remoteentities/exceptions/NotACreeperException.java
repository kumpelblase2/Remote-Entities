package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class NotACreeperException extends Exception
{
	public NotACreeperException()
	{
		super("Entity is not a creeper.");
	}
}
