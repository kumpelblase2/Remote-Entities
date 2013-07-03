package de.kumpelblase2.remoteentities.persistence;

public interface SerializableData
{
	/**
	 * Returns all the parameters that can be serialized
	 *
	 * @return Serializeable parameters
	 */
	public ParameterData[] getSerializableData();
}