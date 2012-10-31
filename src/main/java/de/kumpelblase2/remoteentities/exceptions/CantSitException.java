package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class CantSitException extends Exception
{
	public CantSitException()
	{
		super("Entity is not able to sit.");
	}
}
