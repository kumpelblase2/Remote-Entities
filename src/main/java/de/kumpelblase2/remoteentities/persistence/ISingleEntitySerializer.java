package de.kumpelblase2.remoteentities.persistence;

public interface ISingleEntitySerializer extends IEntitySerializer
{
	/**
	 * Saves the single entity data to a persistent storage
	 *
	 * @param inData Data to save
	 */
	public void save(EntityData inData);

	/**
	 * Loads data for a single entity from the storage with given parameter
	 *
	 * @param inParameter identifier for entity
	 * @return Data for the entity
	 */
	public EntityData load(Object inParameter);
}