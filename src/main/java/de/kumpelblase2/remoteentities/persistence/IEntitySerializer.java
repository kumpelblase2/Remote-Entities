package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface IEntitySerializer
{
	/**
	 * Prepares the saveable data for the given entity
	 *
	 * @param inEntity Entity to prepare the data for
	 * @return The saveable data from the entity
	 */
	public EntityData prepare(RemoteEntity inEntity);

	/**
	 * Saves the data to a persistent storage
	 *
	 * @param inData Data to save
	 * @return true if it could be saved, false if not
	 */
	public boolean save(EntityData[] inData);

	/**
	 * Loads all saved data from the storage
	 *
	 * @return Data in the storage
	 */
	public EntityData[] loadData();

	/**
	 * Creates an entity from the loaded data
	 *
	 * @param inData Data to create an entity from
	 * @return Entity created
	 */
	public RemoteEntity create(EntityData inData);
}