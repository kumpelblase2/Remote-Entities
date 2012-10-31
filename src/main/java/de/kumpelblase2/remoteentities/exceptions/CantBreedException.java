package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class CantBreedException extends Exception
{
	public CantBreedException()
	{
		super("Entity can't breed.");
	}
}
