package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class NotAVillagerException extends Exception
{
	public NotAVillagerException()
	{
		super("Entity is not a villager.");
	}
}
