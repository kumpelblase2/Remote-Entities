package de.kumpelblase2.remoteentities.api.features;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.SerializableData;

public interface Feature extends SerializableData
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

	/**
	 * Method that gets executed when the behavior gets added to the entity
	 */
	public void onAdd();

	/**
	 * Method that gets executed when the behavior gets removed from the entity
	 */
	public void onRemove();
}