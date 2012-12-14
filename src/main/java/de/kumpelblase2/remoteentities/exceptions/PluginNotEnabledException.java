package de.kumpelblase2.remoteentities.exceptions;

@SuppressWarnings("serial")
public class PluginNotEnabledException extends Exception
{
	public PluginNotEnabledException()
	{
		super("RemoteEntities needs to be enable in order to use this operation");
	}
}
