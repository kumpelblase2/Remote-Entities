package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class NotAnAnimalException extends Exception
{
	public NotAnAnimalException()
	{
		super("Entity is not an animal.");
	}
}
