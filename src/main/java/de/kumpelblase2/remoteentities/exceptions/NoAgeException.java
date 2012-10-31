package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class NoAgeException extends Exception
{
	public NoAgeException()
	{
		super("Entity needs an age.");
	}
}
