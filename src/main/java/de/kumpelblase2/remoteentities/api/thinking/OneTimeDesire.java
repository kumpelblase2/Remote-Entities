package de.kumpelblase2.remoteentities.api.thinking;

public interface OneTimeDesire
{
	/**
	 * Checks if the desire is finished and can be removed
	 * 
	 * @return	true if it's done, false if not
	 */
	public boolean isFinished();
}