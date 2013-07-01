package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface Feature
{
	/**
	 * Gets the name of the feature
	 *
	 * @return name
	 */
	public String getName();

	/**
	 * Gets the entity that owns this feature
	 *
	 * @return entity
	 */
	public RemoteEntity getEntity();
}